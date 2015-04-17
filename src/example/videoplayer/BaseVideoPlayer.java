package example.videoplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by Wayne on 2015/4/17.
 *
 */
public abstract class BaseVideoPlayer extends RelativeLayout{
    public BaseVideoPlayer(Context context){
        super(context);

    }
    public BaseVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    /** fix UI here **/
    protected abstract void fixUI();

    private void init(){

    }

}
