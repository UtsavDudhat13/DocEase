package com.example.miniproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.miniproject.R;

public class ImageAdapter extends PagerAdapter {
    Context context;
    int img_list[] = {
            R.drawable.img1,
            R.drawable.img2,
            R.drawable.img3
    };
    int title[] = {
            R.string.title1 ,
            R.string.title2,
            R.string.title3
    };
    int subtitle[] = {
            R.string.subtitle1,
            R.string.subtitle2,
            R.string.subtitle3
    };
    public ImageAdapter(Context context) {
        this.context = context;
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.image_list, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.img);
        TextView mtitle = (TextView) view.findViewById(R.id.tv_title);
        TextView msubtitle = (TextView) view.findViewById(R.id.tv_subtitle);

        imageView.setImageResource(img_list[position]);
        mtitle.setText(title[position]);
        msubtitle.setText(subtitle[position]);

        container.addView(view);

        return view;
    }


    @Override
    public int getCount() {
        return img_list.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
