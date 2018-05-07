package com.dindong.dingdong.presentation.user.wrist;

import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivityBlueWristScanBinding;
import com.dindong.dingdong.manager.SessionMgr;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.wrist.usecase.GetWristCase;
import com.dindong.dingdong.network.api.wrist.usecase.ScanLshCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.wrist.BlueWrist;
import com.dindong.dingdong.util.DensityUtil;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.PermissionUtil;
import com.dindong.dingdong.util.ToastUtil;
//import com.google.zxing.Result;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;

//import me.dm7.barcodescanner.core.IViewFinder;
//import me.dm7.barcodescanner.core.ViewFinderView;
//import me.dm7.barcodescanner.zxing.ZXingScannerView;
import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.core.ViewFinderView;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;
import pub.devrel.easypermissions.EasyPermissions;

public class BlueWristScanActivity extends BaseActivity
    implements ZBarScannerView.ResultHandler, EasyPermissions.PermissionCallbacks {
  ActivityBlueWristScanBinding binding;
  private ZBarScannerView mScannerView;

  public static final String TYPE_ADD = "add";
  public static final String TYPE_INFO = "info";

  private String type;

  String[] perms = {
      Manifest.permission.CAMERA };

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_blue_wrist_scan);

    initScannerView();
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    if (getIntent().getStringExtra(AppConfig.IntentKey.DATA) != null)
      type = getIntent().getStringExtra(AppConfig.IntentKey.DATA);
  }

  @Override
  protected void createEventHandlers() {
    binding.setPresenter(new Presenter());
  }

  @Override
  protected void onResume() {
    super.onResume();
    PermissionUtil.check(this, perms, 100, getString(R.string.permission_req_photo),
        getString(R.string.permission_photo));
    mScannerView.setResultHandler(this);
    mScannerView.startCamera();
  }

  @Override
  protected void onPause() {
    super.onPause();
    mScannerView.stopCamera();
  }

  private void initScannerView() {
    mScannerView = new ZBarScannerView(this) {
      @Override
      protected IViewFinder createViewFinderView(final Context context) {
        return new CustomViewFinderView(context, new CustomViewFinderView.IRectView() {
          @Override
          public void scanRect(Rect rect) {

          }
        });
      }
    };
    mScannerView.setAspectTolerance(0.5f);
    binding.contentFrame.addView(mScannerView);
  }

  private void resumeCamera() {
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        mScannerView.resumeCameraPreview(BlueWristScanActivity.this);
      }
    }, 500);
  }

  @Override
  public void handleResult(Result result) {
    String barcode = result.getContents();
    if (barcode.startsWith(AppConfig.Wrist.BASE_RULE)) {
      getWristInfo(barcode.substring(AppConfig.Wrist.BASE_RULE.length()));
    } else {
      ToastUtil.toastHint(BlueWristScanActivity.this, barcode);
      resumeCamera();
    }

  }

  private void getWristInfo(final String id) {
    new GetWristCase(id).execute(new HttpSubscriber<BlueWrist>(BlueWristScanActivity.this) {
      @Override
      public void onFailure(String errorMsg, Response<BlueWrist> response) {
        DialogUtil.getErrorDialog(BlueWristScanActivity.this, errorMsg).show();
        resumeCamera();
      }

      @Override
      public void onSuccess(Response<BlueWrist> response) {
        final BlueWrist wrist = response.getData();
        if (type.equals(TYPE_ADD)) {
          if (response.getData().getStatus().equals("1")) {
            DialogUtil
                .getConfirmDialog(BlueWristScanActivity.this,
                    getString(R.string.wrist_add_state_bind))
                .setDismissListener(new DialogInterface.OnDismissListener() {
                  @Override
                  public void onDismiss(DialogInterface dialog) {
                    resumeCamera();
                  }
                }).show();

            return;
          }

          Intent intent = new Intent(BlueWristScanActivity.this, BlueWristAddActivity.class);
          intent.putExtra(AppConfig.IntentKey.DATA, response.getData());
          startActivity(intent);
          resumeCamera();
        } else if (type.equals(TYPE_INFO)) {
          if (response.getData().getStatus().equals("0")) {
            DialogUtil
                .getConfirmDialog(BlueWristScanActivity.this,
                    getString(R.string.wrist_add_state_un_bind))
                .setDismissListener(new DialogInterface.OnDismissListener() {
                  @Override
                  public void onDismiss(DialogInterface dialog) {
                    resumeCamera();
                  }
                }).show();
            return;
          }
          new ScanLshCase(wrist.getId(), SessionMgr.getCurrentAdd().getLongitude(),
              SessionMgr.getCurrentAdd().getLatitude())
                  .execute(new HttpSubscriber<Object>(BlueWristScanActivity.this) {
                    @Override
                    public void onFailure(String errorMsg, Response response) {
                      Intent intent = new Intent(BlueWristScanActivity.this,
                          BlueWristDetailActivity.class);
                      intent.putExtra(AppConfig.IntentKey.DATA, wrist);
                      startActivity(intent);
                      resumeCamera();
                    }

                    @Override
                    public void onSuccess(Response response) {
                      Intent intent = new Intent(BlueWristScanActivity.this,
                          BlueWristDetailActivity.class);
                      intent.putExtra(AppConfig.IntentKey.DATA, wrist);
                      startActivity(intent);
                      resumeCamera();
                    }

                  });

        }
      }
    });
  }

  @Override
  public void onPermissionsGranted(int requestCode, List<String> perms) {

    resumeCamera();
  }

  @Override
  public void onPermissionsDenied(int requestCode, List<String> perms) {

  }

  private static class CustomViewFinderView extends ViewFinderView {
    private static final int[] SCANNER_ALPHA = new int[] {
        0, 64, 128, 192, 255, 192, 128, 64 };
    private int scannerAlpha;
    private int count = 0;

    private CustomViewFinderView.IRectView rectViewListener;

    interface IRectView {
      void scanRect(Rect rect);
    }

    public CustomViewFinderView(Context context, CustomViewFinderView.IRectView listener) {
      super(context);
      init();
      this.rectViewListener = listener;
    }

    public CustomViewFinderView(Context context, AttributeSet attrs) {
      super(context, attrs);
      init();
    }

    private void init() {
      setSquareViewFinder(true);
    }

    @Override
    public void onDraw(Canvas canvas) {
      super.onDraw(canvas);
      Rect framingRect = this.getFramingRect();
      if (framingRect != null && rectViewListener != null) {
        rectViewListener.scanRect(framingRect);
        rectViewListener = null;
      }
    }

    @Override
    public void drawLaser(Canvas canvas) {
      Rect framingRect = this.getFramingRect();
      mLaserPaint.setColor(Color.WHITE);
      this.mLaserPaint.setAlpha(SCANNER_ALPHA[this.scannerAlpha]);
      this.scannerAlpha = (this.scannerAlpha + 1) % SCANNER_ALPHA.length;
      int pos = count % framingRect.height() + framingRect.top;
      canvas.drawRect((float) (framingRect.left + 2), (float) (pos - 1),
          (float) (framingRect.right - 1), (float) (pos + 2), this.mLaserPaint);
      count += 10;
      this.postInvalidateDelayed(80L, framingRect.left, framingRect.top, framingRect.right,
          framingRect.bottom);
    }

    @Override
    public void drawViewFinderMask(Canvas canvas) {
      mFinderMaskPaint.setColor(Color.parseColor("#2b2f38"));
      mFinderMaskPaint.setAlpha(170);
      int width = canvas.getWidth();
      int height = canvas.getHeight();
      Rect framingRect = this.getFramingRect();
      canvas.drawRect(0.0F, 0.0F, (float) width, (float) framingRect.top, this.mFinderMaskPaint);
      canvas.drawRect(0.0F, (float) framingRect.top, (float) framingRect.left,
          (float) (framingRect.bottom + 1), this.mFinderMaskPaint);
      canvas.drawRect((float) (framingRect.right + 1), (float) framingRect.top, (float) width,
          (float) (framingRect.bottom + 1), this.mFinderMaskPaint);
      canvas.drawRect(0.0F, (float) (framingRect.bottom + 1), (float) width, (float) height,
          this.mFinderMaskPaint);
    }

    @Override
    public void drawViewFinderBorder(Canvas canvas) {
      Rect framingRect = this.getFramingRect();
      Paint paint = new Paint();
      paint.setColor(Color.parseColor("#979799"));
      paint.setStyle(Paint.Style.STROKE);
      paint.setStrokeWidth(DensityUtil.dip2px(getContext(), 1));
      canvas.drawLine((float) (framingRect.left), (float) (framingRect.top),
          (float) (framingRect.left), (float) (framingRect.bottom), paint);
      canvas.drawLine((float) (framingRect.left), (float) (framingRect.top),
          (float) (framingRect.right), (float) (framingRect.top), paint);
      canvas.drawLine((float) (framingRect.right) + DensityUtil.dip2px(getContext(), 0.5f),
          (float) (framingRect.top),
          (float) (framingRect.right) + DensityUtil.dip2px(getContext(), 0.5f),
          (float) (framingRect.bottom), paint);
      canvas.drawLine((float) (framingRect.left),
          (float) (framingRect.bottom) + DensityUtil.dip2px(getContext(), 0.5f),
          (float) (framingRect.right),
          (float) (framingRect.bottom) + DensityUtil.dip2px(getContext(), 0.5f), paint);

      mBorderPaint.setColor(Color.parseColor("#fa364e"));
      mBorderPaint.setStrokeWidth(DensityUtil.dip2px(getContext(), 4));
      float width = mBorderPaint.getStrokeWidth();

      int deta = DensityUtil.dip2px(getContext(), 1);

      canvas.drawLine((framingRect.left + width / 2) - deta, (float) (framingRect.top) - deta,
          (framingRect.left + width / 2) - deta,
          (float) (framingRect.top + this.mBorderLineLength) - deta, this.mBorderPaint);
      canvas.drawLine((float) (framingRect.left) - deta, (framingRect.top + width / 2) - deta,
          (float) (framingRect.left + this.mBorderLineLength) - deta,
          (framingRect.top + width / 2) - deta, this.mBorderPaint);
      canvas.drawLine((framingRect.left + width / 2) - deta, (float) (framingRect.bottom) + deta,
          (framingRect.left + width / 2) - deta,
          (float) (framingRect.bottom - this.mBorderLineLength) + deta, this.mBorderPaint);
      canvas.drawLine((float) (framingRect.left) - deta, (framingRect.bottom - width / 2) + deta,
          (float) (framingRect.left + this.mBorderLineLength) - deta,
          (framingRect.bottom - width / 2) + deta, this.mBorderPaint);
      canvas.drawLine((framingRect.right - width / 2) + deta, (float) (framingRect.top) - deta,
          (framingRect.right - width / 2) + deta,
          (float) (framingRect.top + this.mBorderLineLength) - deta, this.mBorderPaint);
      canvas.drawLine((float) (framingRect.right) + deta, (framingRect.top + width / 2) - deta,
          (float) (framingRect.right - this.mBorderLineLength) + deta,
          (framingRect.top + width / 2) - deta, this.mBorderPaint);
      canvas.drawLine((framingRect.right - width / 2) + deta, (float) (framingRect.bottom) + deta,
          (framingRect.right - width / 2) + deta,
          (float) (framingRect.bottom - this.mBorderLineLength) + deta, this.mBorderPaint);
      canvas.drawLine((float) (framingRect.right) + deta, (framingRect.bottom - width / 2) + deta,
          (float) (framingRect.right - this.mBorderLineLength) + deta,
          (framingRect.bottom - width / 2) + deta, this.mBorderPaint);

    }

  }

  public class Presenter {
    public void onBack() {
      if (mScannerView.getFlash()) {
        mScannerView.toggleFlash();
      }
      onBackPressed();
    }

    public void toggleFlash() {
      if (mScannerView.getFlash()) {
        binding.toggleFlash.setImageResource(R.mipmap.ic_flash_wht_off);
      } else {
        binding.toggleFlash.setImageResource(R.mipmap.ic_flash_wht_on);
      }
      mScannerView.toggleFlash();
    }
  }
}
