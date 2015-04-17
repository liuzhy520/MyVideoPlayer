package example.videoplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Wayne on 2015/4/17.
 */
public class BaseVideoPlayer extends RelativeLayout{
    public BaseVideoPlayer(Context context){
        super(context);
    }
    public BaseVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

}
