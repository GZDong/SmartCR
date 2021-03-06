package com.oocl.johngao.smartcr.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.oocl.johngao.smartcr.Adapter.ConListAdapter;
import com.oocl.johngao.smartcr.Adapter.PicListAdapter;
import com.oocl.johngao.smartcr.Const.Const;
import com.oocl.johngao.smartcr.Data.Container;
import com.oocl.johngao.smartcr.Data.Pictures;
import com.oocl.johngao.smartcr.MyView.SideBar;
import com.oocl.johngao.smartcr.MyView.TCodeView;
import com.oocl.johngao.smartcr.MyView.TCodeView2;
import com.oocl.johngao.smartcr.R;
import com.oocl.johngao.smartcr.ToolsClass.CalUtils;
import com.oocl.johngao.smartcr.ToolsClass.CameraPreview;
import com.oocl.johngao.smartcr.ToolsClass.DataLab;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.security.Policy;
import java.util.ArrayList;
import java.util.List;

public class TakePhotoActivity extends AppCompatActivity implements CameraPreview.OnCaptureListener{

    public static final String TAG = "TakePhotoActivity";
    private CameraPreview mPreview;
    private Button mTakePictureBtn;
    private FrameLayout mCameraLayout;

    private RecyclerView mRecyclerView;
    private PicListAdapter mPicListAdapter;
    private List<Pictures> mPicList;


    private TextView mNameText;

    private DataLab mDataLab;
    private BroadcastReceiver mBroadcastReceiver;

    private ImageButton mImageButton;
    private ImageView mDialogBG;
    private TextView mFinishText;

    private int flag = 0;
    private boolean swtch = false;

    private String mTag;
    private String ConNo;

    private List<Container> mContainerList;

    private ImageButton mNextBtn;
    private OnNextBtnListener mOnNextBtnListener;

    private TCodeView mTCodeView;
    private TCodeView2 mTCodeView2;
    private TextView mSureText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_take_photo);
        Log.e(TAG, "onCreate: ");
        mDataLab = DataLab.get(this);
        mContainerList = new ArrayList<>();
        mContainerList = mDataLab.getContainerList(1);

        Intent intent = getIntent();
        ConNo = intent.getStringExtra("ConNo");
        mTag = intent.getStringExtra("Message");
        setOnNextBtnListener(mDataLab.getConListAdapter());
        Log.e(TAG, "onCreate: ....." + ConNo + mTag);

        //检查相机是否可用
        if (!checkCameraHardware(this)) {
            Toast.makeText(this, "相机不支持", Toast.LENGTH_SHORT).show();
        }else {
            Log.e(TAG, "onCreate: Here, start to init Camera :");
            //初始化相机
            initCamera();
        }


        initList();
        initView();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.oocl.john.fresh");
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e(TAG, "onReceive: 拍照完用于刷新照片列表和重启手电筒的接受器");
                int index = intent.getIntExtra("index",0);
                mPicListAdapter.s = index + 1;
                mPicListAdapter.notifyDataSetChanged();
                mRecyclerView.scrollToPosition(mPicList.size()-1);
                if (swtch == true){
                    Intent intent1 = new Intent("com.oocl.john.switchlight");
                    intent1.putExtra("sign",false);
                    sendBroadcast(intent1);
                    Intent intent2 = new Intent("com.oocl.john.switchlight");
                    intent2.putExtra("sign",true);
                    sendBroadcast(intent2);
                    Log.e(TAG, "onReceive: 线程执行完，先发送关灯广播，再发送开灯广播");
                }else {
                    Intent intent1 = new Intent("com.oocl.john.switchlight");
                    intent1.putExtra("sign",false);
                    sendBroadcast(intent1);
                    Log.e(TAG, "onReceive: 线程执行完再发送关灯广播");
                }
            }
        };
        registerReceiver(mBroadcastReceiver,intentFilter);
    }


    private void initList() {
        mPicList = new ArrayList<>();
        //*第一处：ConNo，TCode用于决定加载的数据列表
        mDataLab.initPicList(ConNo,mTag);
        mPicList = mDataLab.getPicturesList();
        int count = 0;
        for (Pictures pictures: mPicList){
            if (!pictures.getConNo().equals(Const.NullConNo)){
                count ++;
            }
        }
        mPreview.setCount(count);
        mPreview.setTCode(CalUtils.calConsTCodeFromTag(mTag));   //发送给PreView
    }

    //判断相机是否支持
    private boolean checkCameraHardware(Context context) {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    //初始化相机
    public void initCamera(){
        mPreview = new CameraPreview(TakePhotoActivity.this);
        //**第二处：ConNo，TCode用于决定储存的位置和图片的命名

        mPreview.setConNo(ConNo);

        mPreview.setOnCaptureListener(this);
        /*mPreview.setOnTouchListener(new View.OnTouchListener() {   //设置聚焦
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mCamera.autoFocus(null);
                return false;
            }
        });*/
        mCameraLayout = (FrameLayout) findViewById(R.id.camera_preview);
        mCameraLayout.addView(mPreview);
    }

    public void initView(){
        mPicListAdapter = new PicListAdapter(this, mPicList,CalUtils.calConsTCodeFromTag(mTag));
        mRecyclerView = (RecyclerView) findViewById(R.id.picture_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mPicListAdapter);
        mRecyclerView.scrollToPosition(mPicList.size()-1);

        //mCameraPreview = (CameraPreview) findViewById(R.id.camera_view);

        mTakePictureBtn = (Button) findViewById(R.id.btn_capture);
        mTakePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*mCamera.autoFocus(mAutoFocusCallback);*/
                mPreview.takePicture();
            }
        });


        mDialogBG = (ImageView) findViewById(R.id.dialog_bg);

        mDialogBG.getBackground().setAlpha(230);


        mNameText = (TextView) findViewById(R.id.picture_name);

        mImageButton = (ImageButton) findViewById(R.id.light_btn);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swtch == true){
                    Intent intent = new Intent("com.oocl.john.switchlight");
                    intent.putExtra("sign",false);
                    sendBroadcast(intent);
                    Log.e(TAG, "onClick: 在左下角点击后发送关灯广播");
                    swtch = false;
                    mImageButton.setImageResource(R.drawable.offs);
                }else {
                    Intent intent = new Intent("com.oocl.john.switchlight");
                    intent.putExtra("sign",true);
                    sendBroadcast(intent);
                    Log.e(TAG, "onClick: 在左下角点击后发送开灯广播");
                    swtch = true;
                    mImageButton.setImageResource(R.drawable.ons);
                }
            }
        });

        mFinishText = (TextView) findViewById(R.id.finish_text);
        mFinishText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        mNextBtn = (ImageButton) findViewById(R.id.next_btn);
        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataLab.setNextbt(true);
                finish();


                /*int i;
                for (i = 0; i < mContainerList.size()-1;i++){
                    Container container = mContainerList.get(i);
                    if (container.getConNo().equals(ConNo)&&i!=mContainerList.size()-1){
                        String s1 = CalUtils.calConsWTCode(mContainerList.get(i+1));
                        String s2 = CalUtils.calConsRTCode(mContainerList.get(i+1));
                        if (s1.equals(transTagToTCode(mTag))||s2.equals(transTagToTCode(mTag))){
                            Intent intent = new Intent(TakePhotoActivity.this,TakePhotoActivity.class);
                            intent.putExtra("ConNo",ConNo);
                            intent.putExtra("Message",mTag);

                            finish();
                            startActivity(intent);
                        }
                    }
                }
                if (i>=mContainerList.size()){
                    Toast.makeText(TakePhotoActivity.this,"没有下一个货柜了！",Toast.LENGTH_LONG).show();
                }*/
            }
        });

        mSureText = (TextView) findViewById(R.id.be_sure_text);
        if (mPicList.size()>0&& !mPicList.get(0).getConNo().equals(Const.NullConNo) && (mTag.equals("WashBeforeFinishWashAfterWithZero")||mTag.equals("WashBeforeFinishWashAfterProgress")||
                mTag.equals("RepairBeforeFinishRepairAfterWithZero")||mTag.equals("RepairBeforeFinishRepairAfterProgress"))){
            String T = mPicList.get(0).getTCode();
            switch (T){
                case "WY":
                    mSureText.setText(Const.Wash);
                    mPreview.setTCode("WY");
                    break;
                case "P":
                    mSureText.setText(Const.PWash);
                    mPreview.setTCode("P");
                    break;
                case "NIL":
                    mSureText.setText(Const.NWash);
                    mPreview.setTCode("NIL");
                    break;
                case "C":
                    mSureText.setText(Const.CWash);
                    mPreview.setTCode("C");
                    break;

                case Const.IICL1:
                    mSureText.setText(Const.IICL1);
                    mPreview.setTCode(Const.IICL1);
                    break;
                case Const.IICL2:
                    mSureText.setText(Const.IICL2);
                    mPreview.setTCode(Const.IICL2);
                    break;
                case Const.IICL3:
                    mSureText.setText(Const.IICL3);
                    mPreview.setTCode(Const.IICL3);
                    break;
                case Const.IICL4:
                    mSureText.setText(Const.IICL4);
                    mPreview.setTCode(Const.IICL4);
                    break;
            }
            mSureText.setVisibility(View.VISIBLE);
        }

        mTCodeView = (TCodeView) findViewById(R.id.tcode_view);
        mTCodeView.setOnTabClickListener(new TCodeView.OnTabClickListener() {
            @Override
            public void onTabClick(String var1) {
                String tCode = CalUtils.calConsTCodeFromTag(mTag);

                if (tCode.equals("WY")){
                    switch (var1){
                        case "水洗":
                            mPreview.setTCode("WY");
                            break;
                        case "化学洗":
                            mPreview.setTCode("C");
                            break;
                        case "除钉":
                            mPreview.setTCode("NIL");
                            break;
                        case "除纸":
                            mPreview.setTCode("P");
                            break;
                        default:
                            break;
                    }
                    mSureText.setText(var1);
                }

            }
        });
        mTCodeView2 = (TCodeView2) findViewById(R.id.tcode_view_R);
        mTCodeView2.setOnTabClickListener(new TCodeView2.OnTabClickListener() {
            @Override
            public void onTabClick(String var1) {
                String tCode = CalUtils.calConsTCodeFromTag(mTag);

                if (tCode.equals(Const.IICL1)){
                    switch (var1){
                        case Const.IICL1:
                            mPreview.setTCode(Const.IICL1);
                            break;
                        case Const.IICL2:
                            mPreview.setTCode(Const.IICL2);
                            break;
                        case Const.IICL3:
                            mPreview.setTCode(Const.IICL3);
                            break;
                        case Const.IICL4:
                            mPreview.setTCode(Const.IICL4);
                            break;
                        default:
                            break;
                    }
                    mSureText.setText(var1);
                }

            }
        });

        if (mPicList.get(0).getConNo().equals(Const.NullConNo) &&(mTag.equals("WashBeforeFinishWashAfterWithZero")||mTag.equals("WashBeforeFinishWashAfterProgress")) ){
            mTCodeView.setVisibility(View.VISIBLE);
            mSureText.setText(Const.Wash);
        }
        if (mPicList.get(0).getConNo().equals(Const.NullConNo) &&( mTag.equals("RepairBeforeFinishRepairAfterWithZero")||mTag.equals("RepairBeforeFinishRepairAfterProgress"))){
            mTCodeView2.setVisibility(View.VISIBLE);
            mSureText.setText(Const.IICL1);
        }

    }
    //聚焦回调
    /*private android.hardware.Camera.AutoFocusCallback mAutoFocusCallback = new android.hardware.Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, android.hardware.Camera camera) {
            if (success){
                mCamera.takePicture(null,null,mPictureCallback);
            }
        }
    };*/

    //拍照时触发的回调接口方法
    @Override
    public void onCapture(String name, int seq) {
        mNameText.setText(name);
        mNameText.setVisibility(View.VISIBLE);

        if ((CalUtils.calConsTCodeFromTag(mTag).equals(Const.IICL1)||CalUtils.calConsTCodeFromTag(mTag).equals("WY"))){
            mSureText.setVisibility(View.VISIBLE);
            mTCodeView.setVisibility(View.GONE);
            mTCodeView2.setVisibility(View.GONE);
        }

        /*mPicListAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(mPicList.size()-1);*/
    }
    //siderbar的回调接口方法
    /*@Override
    public void onTouchingLetterChanged(String s) {
        Log.e(TAG, "onTouchingLetterChanged: 触发回调接口后s="+s );
        String tCode = CalUtils.calConsTCodeFromTag(mTag);

        if (tCode.equals("WY")){
            switch (s){
                case "水洗":
                    mPreview.setTCode("WY");
                    break;
                case "化学洗":
                    mPreview.setTCode("C");
                    break;
                case "除钉":
                    mPreview.setTCode("NIL");
                    break;
                case "除纸":
                    mPreview.setTCode("P");
                    break;
                default:
                    break;
            }
        }
        Log.e(TAG, "onTouchingLetterChanged: TCode转化后为" + tCode );
    }*/

    @Override
    protected void onStop() {
        super.onStop();
        flag = 1;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (flag ==1){
            mCameraLayout.removeAllViews();
            mDataLab = DataLab.get(this);

            //检查相机是否可用
            if (!checkCameraHardware(this)) {
                Toast.makeText(this, "相机不支持", Toast.LENGTH_SHORT).show();
            }else {
                //初始化相机
                initCamera();
            }

            initList();
            initView();
            flag = 0;
            //充值手电筒开关
            if (swtch == true) {
                swtch = false;
                mImageButton.setImageResource(R.drawable.offs);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
        unregisterReceiver(mBroadcastReceiver);
        //单例模式，必须要重置列表
        mDataLab.resetPicturesListNull();

        if (mDataLab.nextbt == true){
            mOnNextBtnListener.onNextBtn(ConNo,mTag);
            mDataLab.setNextbt(false);
        }


    }


    public interface OnNextBtnListener{
        void onNextBtn(String ConNo,String TCode);
    }

    public void setOnNextBtnListener(OnNextBtnListener nextBtnListener){
        mOnNextBtnListener = nextBtnListener;
    }

}
