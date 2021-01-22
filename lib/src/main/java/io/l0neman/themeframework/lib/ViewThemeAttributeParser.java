package io.l0neman.themeframework.lib;

import android.util.AttributeSet;
import android.view.View;

import androidx.collection.ArrayMap;

import java.util.Map;

/**
 * View 主题属性解析器，可从 View 对应的 AttributeSet 中解析一个 View 中的主题属性
 */
class ViewThemeAttributeParser {
  private final Map<Class<? extends View>, ThemeAttributeAdapter<? extends View>> mThemeAttributeCache =
      new ArrayMap<>();

  // 对于同类型的 View，先复用 ThemeAttribute，为了使用它的 parse 方法解析主题属性，如果没有主题属性，
  // 那么每次创建 ThemeAttribute 对象将是一种浪费，所以进行复用，一旦解析到主题属性（parse 返回 true），
  // 那么将这个 ThemeAttribute 移除，下次不再复用这个对象了，将使用新的对象进行复用，直到下一次解析到主题属性
  private ThemeAttributeAdapter<? extends View> getThemeAttributeCacheByViewType(Class<? extends View> viewType) {
    ThemeAttributeAdapter<? extends View> themeAttributeAdapter = mThemeAttributeCache.get(viewType);
    if (themeAttributeAdapter == null) {
      ThemeAttributeAdapterManager.ThemeAttributeAdapterFactory<? extends View> attributeFactory =
          ThemeAttributeAdapterManager.getFactory(viewType);
      try {
        // 可能出现异常
        themeAttributeAdapter = attributeFactory.create();
        mThemeAttributeCache.put(viewType, themeAttributeAdapter);
      } catch (RuntimeException e) {
        e.printStackTrace();
        return null;
      }
    }

    return themeAttributeAdapter;
  }

  // 将上一个对象从缓存中分离出来，不再给下一次复用了，表示调用者想要将这个对象据为己有
  private void detachLastThemeAttributeFromCacheByViewType(Class<? extends View> viewType) {
    // 移除后，下一次就会重新创建新的 ThemeAttribute 了
    mThemeAttributeCache.remove(viewType);
  }

  /**
   * 解析 View 中的主题属性，如果包含主题属性，将 View 和主题属性添加到映射表
   *
   * @param view         View
   * @param attributeSet 从 AttributeSet 中解析属性
   *
   * @return 如果为 null，则 View 不包含主题属性
   */
  public ThemeAttributeAdapter<? extends View> parse(View view, AttributeSet attributeSet) {
    final Class<? extends View> viewType = view.getClass();
    ThemeAttributeAdapter<? extends View> themeAttributeAdapter = getThemeAttributeCacheByViewType(viewType);

    if (themeAttributeAdapter != null && themeAttributeAdapter.parse(view, attributeSet)) {
      detachLastThemeAttributeFromCacheByViewType(viewType);
      return themeAttributeAdapter;
    }

    return null;
  }
}
