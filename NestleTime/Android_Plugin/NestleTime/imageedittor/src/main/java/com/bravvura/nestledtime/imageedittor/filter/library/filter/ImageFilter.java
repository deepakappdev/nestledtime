package com.bravvura.nestledtime.imageedittor.filter.library.filter;

import android.content.Context;
import android.opengl.GLES10;
import com.bravvura.nestledtime.imageedittor.R;
import com.bravvura.nestledtime.imageedittor.filter.library.gles.GlUtil;
public class ImageFilter extends CameraFilter {
    public ImageFilter(Context applicationContext) {
        super(applicationContext);
    }

    @Override public int getTextureTarget() {
        return GLES10.GL_TEXTURE_2D;
    }

    @Override protected int createProgram(Context applicationContext) {
        return GlUtil.createProgram(applicationContext, R.raw.vertex_shader,
                R.raw.fragment_shader_2d);
    }
}
