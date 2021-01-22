package io.l0neman.themeframework.lib;

import android.view.View;

import androidx.annotation.AttrRes;

/**
 * 根据属性值为设置 View 属性的接口
 *
 * @param <T> View 类型泛型
 */
public interface AttributeSetter<T extends View> {
  /**
   * 根据传入的属性值设置 View
   *
   * @param view      View
   * @param attribute 属性值
   *
   * @return 设置成功为 true，否则为 false
   */
  boolean setView(T view, @AttrRes int attribute);
}
