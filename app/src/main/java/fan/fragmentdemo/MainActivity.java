package fan.fragmentdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fan.fragmentdemo.fragment.OneFragment;
import fan.fragmentdemo.fragment.ThreeFragment;
import fan.fragmentdemo.fragment.TwoFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //当前显示的fragment
    private static final String CURRENT_FRAGMENT = "STATE_FRAGMENT_SHOW";

    private TextView tvone;
    private TextView tvtwo;
    private TextView tvthree;
    private FragmentManager fragmentManager;

    private Fragment currentFragment = new Fragment();
    private List<Fragment> fragments = new ArrayList<>();

    private int currentIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.tvthree = (TextView) findViewById(R.id.tv_three);
        this.tvtwo = (TextView) findViewById(R.id.tv_two);
        this.tvone = (TextView) findViewById(R.id.tv_one);

        fragmentManager = getSupportFragmentManager();

        tvthree.setOnClickListener(this);
        tvtwo.setOnClickListener(this);
        tvone.setOnClickListener(this);


        if (savedInstanceState != null) { // “内存重启”时调用

            //获取“内存重启”时保存的索引下标
            currentIndex = savedInstanceState.getInt(CURRENT_FRAGMENT,0);

            fragments.removeAll(fragments);
            fragments.add(fragmentManager.findFragmentByTag(0+""));
            fragments.add(fragmentManager.findFragmentByTag(1+""));
            fragments.add(fragmentManager.findFragmentByTag(2+""));

            //恢复fragment页面
            restoreFragment();


        }else{      //正常启动时调用

            fragments.add(new OneFragment());
            fragments.add(new TwoFragment());
            fragments.add(new ThreeFragment());

            showFragment();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        //“内存重启”时保存当前的fragment名字
        outState.putInt(CURRENT_FRAGMENT,currentIndex);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.tv_one:

                currentIndex = 0;

                break;
            case R.id.tv_two:

                currentIndex = 1;

                break;
            case R.id.tv_three:

                currentIndex = 2;

                break;

        }

        showFragment();

    }


    /**
     * 使用show() hide()切换页面
     * 显示fragment
     */
    private void showFragment(){

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //如果之前没有添加过
        if(!fragments.get(currentIndex).isAdded()){
            transaction
                    .hide(currentFragment)
                    .add(R.id.content,fragments.get(currentIndex),""+currentIndex);  //第三个参数为添加当前的fragment时绑定一个tag

        }else{
            transaction
                    .hide(currentFragment)
                    .show(fragments.get(currentIndex));
        }

        currentFragment = fragments.get(currentIndex);

        transaction.commit();

    }

    /**
     * 恢复fragment
     */
    private void restoreFragment(){

        FragmentTransaction mBeginTreansaction = fragmentManager.beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {

            if(i == currentIndex){
                mBeginTreansaction.show(fragments.get(i));
            }else{
                mBeginTreansaction.hide(fragments.get(i));
            }
        }

        mBeginTreansaction.commit();

        //把当前显示的fragment记录下来
        currentFragment = fragments.get(currentIndex);

    }
}
