package com.bravvura.nestledtime.imageedittor.filter.library.filter.BlurFilter;

import android.content.Context;
import android.opengl.GLES20;

import com.bravvura.nestledtime.imageedittor.R;
import com.bravvura.nestledtime.imageedittor.filter.library.gles.GlUtil;

class ImageFilterGaussianSingleBlur extends CameraFilterGaussianSingleBlur {

    public ImageFilterGaussianSingleBlur(Context applicationContext, float blurRatio,
            boolean widthOrHeight) {
        super(applicationContext, blurRatio, widthOrHeight);
    }
    
    @Override public int getTextureTarget() {
        return GLES20.GL_TEXTURE_2D;
    }

    @Override protected int createProgram(Context applicationContext) {
        return GlUtil.createProgram(applicationContext, R.raw.vertex_shader_blur,
                R.raw.fragment_shader_2d_blur);
    }
}
