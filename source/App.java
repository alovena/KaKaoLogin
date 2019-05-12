package cehs0703.seo.kakaologin;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.kakao.util.helper.Utility.getPackageInfo;

/**
 * Created by q on 2019-05-12.
 * 필수 권독 : 카카오 개발자 홈페이지에 앱의 SDK(Hash 키 등록)와 환경설정은 반드시 하고 이부분을 넘어오셔야합니다
 * 필수 권독 : App파일 만드셔서 붙여넣기 하시면됩니다..
 * 필수 권독 : 제가쓴 카카오 SDK 버전은 앞에 사진에 나와있듯 1.4.1 입니다. 현재 1.17과는 메서드 변경 부분이 있어서 
 * 필수 권독 : SDK 버전을 최신 버전과 맞추게 되면 static 메서드 오류가 뜨는데 getInstace()를 붙여주시면 됩니다.( getInstance().requestme() ) 
 */

public class App extends Application {

    private class KakaoSDKAdapter extends KakaoAdapter {

        @Override
        public ISessionConfig getSessionConfig() {
            return new ISessionConfig() {
                @Override
                public AuthType[] getAuthTypes() {
                    return new AuthType[] {AuthType.KAKAO_LOGIN_ALL};
                }

                @Override
                public boolean isUsingWebviewTimer() {
                    return false;
                }

                @Override
                public boolean isSecureMode() {
                    return false;
                }

                @Override
                public ApprovalType getApprovalType() {
                    return ApprovalType.INDIVIDUAL;
                }

                @Override
                public boolean isSaveFormData() {
                    return true;
                }
            };
        }

        @Override
        public IApplicationConfig getApplicationConfig() {
            return new IApplicationConfig() {
                @Override
                public Context getApplicationContext() {
                    return App.this.getApplicationContext();
                }
            };
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        KakaoSDK.init(new KakaoSDKAdapter());
        Log.e("Key Hash : ", getKeyHash(this));
    }

    public  String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.e("HHASH", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }
}

