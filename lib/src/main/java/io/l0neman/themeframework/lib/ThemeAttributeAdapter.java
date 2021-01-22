package io.l0neman.themeframework.lib;

import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleableRes;
import androidx.collection.ArrayMap;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 主题属性适配器基类型，为一个 View 类型具有的主题属性进行适配
 * <p>
 * 为一个 View 类型提供所支持的主题属性列表，同时负责处理这些主题属性的设置
 * <p>
 * 实现一个具体 View 类型的适配器需要实现子类
 *
 * @param <T> View 具体类型，可约束 setView 方法时针对具体类型的 View，不必手动进行类型转换
 */
public abstract class ThemeAttributeAdapter<T extends View> {
  private static final String TAG = ThemeAttributeAdapter.class.getSimpleName();

  private ThemeResources mThemeResources;
  private final Map<Integer, Integer> mThemeAttributesValues = new ArrayMap<>();
  private OnSetViewListener<T> mOnSetViewListener;

  /**
   * 提供自定义的主题属性
   *
   * @return 一组属性，List 是为了子类容易服用父类型属性
   */
  @NonNull public abstract Collection<Integer> themeAttributes();

  /**
   * 根据主题属性类型将具体属性值对包含的图像或色彩资源设置到 View 上
   *
   * @param view           设置主题的目标 View
   * @param themeAttribute 自定义的主题属性
   * @param attributeValue 布局中的主题属性具体资源值
   *
   * @return 是否处理了主题属性
   */
  public abstract boolean setView(T view,
                                  @StyleableRes int themeAttribute,
                                  @AttrRes int attributeValue);

  public interface OnSetViewListener<T extends View> {
    void onSetView(T view, ThemeResources themeResources);
  }

  public void setOnSetViewListener(OnSetViewListener<T> mOnSetViewListener) {
    this.mOnSetViewListener = mOnSetViewListener;
  }

  /**
   * 动态添加主题属性以及对应属性值，适用于非布局解析的情况
   *
   * @param themeAttribute 主题属性 R.styleable.Attributes_Layout_xx
   * @param attributeValue 属性值 R.attr.xx
   */
  public final void setAttribute(@StyleableRes int themeAttribute, @AttrRes int attributeValue) {
    mThemeAttributesValues.put(themeAttribute, attributeValue);
  }

  /**
   * 解析 View 中的主题属性
   *
   * @param view         View
   * @param attributeSet 从 AttributeSet 中进行解析
   *
   * @return 解析到主题属性则返回 true，否则为 false
   */
  public final boolean parse(View view, AttributeSet attributeSet) {
    TypedArray a = view.getContext().obtainStyledAttributes(attributeSet, R.styleable.Attributes_Layout);

    Collection<Integer> attributes = themeAttributes();
    if (attributes.isEmpty()) {
      a.recycle();
      TfDebug.logD(TAG, "view: " + view + " has no theme attributes");
      return false;
    }

    try {
      for (int themeAttribute : attributes) {
        String layoutAttribute = a.getString(themeAttribute);
        if (!TextUtils.isEmpty(layoutAttribute)) {
          int attributeByName = ThemeResources.getAttributeByName(view.getContext(), layoutAttribute);
          if (attributeByName == 0) {
            Log.w(TAG, "theme attribute: " + layoutAttribute + " is invalid");
            continue;
          }

          mThemeAttributesValues.put(themeAttribute, attributeByName);
          TfDebug.logD(TAG, "view: " + view + " has theme attribute: " + layoutAttribute);
        }
      }

      boolean force = a.getBoolean(R.styleable.Attributes_Layout_tf_force, false);
      return force || !mThemeAttributesValues.isEmpty();
    } catch (Throwable e) {
      throw new RuntimeException(e);
    } finally {
      a.recycle();
    }
  }

  /**
   * 将主题属性设置到 View 上
   *
   * @param view           View
   * @param themeResources 提供一个主题资源供已获取当前主题包含的资源
   */
  public final void setView(View view, ThemeResources themeResources) {
    mThemeResources = themeResources;

    T castView;
    try {
      // noinspection unchecked: use exception already
      castView = (T) view;
    } catch (ClassCastException e) {
      throw new RuntimeException(e);
    }

    if (mOnSetViewListener != null)
      mOnSetViewListener.onSetView(castView, getThemeResources());

    Set<Map.Entry<Integer, Integer>> entries = mThemeAttributesValues.entrySet();
    for (Map.Entry<Integer, Integer> themeAttributeValueMap : entries) {
      final int layoutAttribute = themeAttributeValueMap.getValue();
      final int themeAttribute = themeAttributeValueMap.getKey();

      try {
        if (setView(castView, themeAttribute, layoutAttribute))
          TfDebug.logD(TAG, "set view: " + view + " to attribute: " + themeAttributeValueMap);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 提供一个主题资源对象
   *
   * @param mThemeResources 主题资源对象
   */
  public void setThemeResources(ThemeResources mThemeResources) {
    this.mThemeResources = mThemeResources;
  }

  /** 获取主题资源对象 */
  public ThemeResources getThemeResources() {
    return mThemeResources;
  }
}
