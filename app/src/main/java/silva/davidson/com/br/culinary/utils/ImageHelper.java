package silva.davidson.com.br.culinary.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageHelper  {
    public static void loadImage(Context context, String url, final ImageView imageView) {
        Glide.with(context)
                .load(url)
                .into(imageView);
    }
}
