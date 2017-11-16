package net.flow9.rxbasic04_binding;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import java.util.Random;

import io.reactivex.Observable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 0. 로그인 체크하기
        Observable<TextViewTextChangeEvent> idEmitter = RxTextView.textChangeEvents(findViewById(R.id.editId));
        Observable<TextViewTextChangeEvent> pwEmitter = RxTextView.textChangeEvents(findViewById(R.id.editPw));

        // 조건 id 가 5자이상이고, pw가 8자이상면 btnSignin 의 enable 을 true 로 아니면 false
        Observable.combineLatest(
                idEmitter,
                pwEmitter,
                (TextViewTextChangeEvent id,TextViewTextChangeEvent pw) -> {
                    boolean idCheck = id.text().length() >= 5;
                    boolean pwCheck = pw.text().length() >= 8;

                    if(idCheck && pwCheck)
                        return true;
                    else
                        return false;
                }
        ).subscribe(
                (Boolean flag) -> {
                    findViewById(R.id.btnSignin).setEnabled(flag);
                }
        );





        // 1. editText에 입력되는 값을 체크해서 실시간으로 Log를 뿌려준다.
        RxTextView.textChangeEvents(findViewById(R.id.editText))
                .subscribe(ch-> Log.i("RxBinding","word:"+ch.text()));

        // 2. 버튼을 클릭하면 editText에 Random 숫자를 입력
        RxView.clicks(findViewById(R.id.button))
                // button 에는 Button 타입의 오브젝트가 리턴되는데, 이를 문자타입으로 변경
                .map(button->new Random().nextInt()+"")
                // subscribe 에서는 map 에서 변형된 타입을 받게 된다.
                .subscribe(number->{
                    ((EditText)findViewById(R.id.editText)).setText(number);
                });
    }
}
