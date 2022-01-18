package com.cookandroid.s2_archiving

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ResisterActivity : AppCompatActivity() {

    private lateinit var mFirebaseAuth: FirebaseAuth // 파이어베이스 인증 처리
    private lateinit var mDatabaseRef: DatabaseReference // 실시간 데이터 베이스
    private lateinit var mEtEmail: EditText // 회원 가입 입력 필드(이메일)
    private lateinit var mEtNickname:EditText // 닉네임 입력 필드
    private lateinit var mEtPwd: EditText // 회원 가입 입력 필드(비밀번호)
    private lateinit var mEtPwdCheck: EditText // 비밀번호 확인 필드
    private lateinit var mBtnRegister: Button // 회원 가입 버튼

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resister)

        mFirebaseAuth = FirebaseAuth.getInstance()
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Firebase")
        mEtEmail = findViewById<EditText>(R.id.etEmail)
        mEtNickname=findViewById<EditText>(R.id.etNickname)
        mEtPwd = findViewById<EditText>(R.id.etPwd)
        mEtPwdCheck = findViewById<EditText>(R.id.etPwdCheck)
        mBtnRegister = findViewById<Button>(R.id.btnRegister)


        mBtnRegister.setOnClickListener(View.OnClickListener {
            // 회원가입 처리 시작
            var strEmail: String = mEtEmail.getText().toString()
            var strNickname: String = mEtNickname.getText().toString()
            var strPhone: String =""
            var strPwd: String = mEtPwd.getText().toString()
            var strPwdCheck: String = mEtPwdCheck.getText().toString()

            if(strEmail.equals("")||strNickname.equals("")||strPwd.equals("")||strPwdCheck.equals("")){
                Toast.makeText(this, "모든 항목을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
            else if(!strPwd.equals(strPwdCheck)){
                Toast.makeText(this, "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
            }
            else {

                mFirebaseAuth?.createUserWithEmailAndPassword(strEmail, strPwd)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            var firebaseUser: FirebaseUser? = mFirebaseAuth.currentUser
                            var account = UserAccount()
                            account.userId = firebaseUser?.uid.toString()
                            account.userEmail = firebaseUser?.email.toString()
                            account.userNickname = strNickname
                            account.userPhone = strPhone
                            account.userPwd = strPwd

                            // setValue : database에 insert (삽입) 행위위
                            mDatabaseRef.child("Firebase").child(firebaseUser?.uid.toString())
                                .setValue(account)

                            Toast.makeText(this, "회원가입에 성공하셨습니다", Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(this, "회원가입에 실패하셨습니다", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        })

    }

}
