package example.videoplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Wayne on 2015/4/17.
 */
public class VideoPlayer extends BaseVideoPlayer {

    public VideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void fixLandscapeUI() {
        Toast.makeText(context, "Landscapte", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void fixPortraitUI() {
        Toast.makeText(context, "Portrait", Toast.LENGTH_SHORT).show();
    }


}
