package io.l0neman.themeframework.lib;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.collection.ArrayMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * 布局主题属性收集器，收集一个布局中的 View 对应的主题属性信息，
 * 布局的遍历过程参考了 {@link android.view.LayoutInflater} 的代码
 */
class LayoutThemeAttributesCollector {
  private static final String TAG = LayoutThemeAttributesCollector.class.getSimpleName();

  private static final String TAG_MERGE = "merge";
  private static final String TAG_INCLUDE = "include";
  private static final String TAG_REQUEST_FOCUS = "requestFocus";
  private static final String TAG_1995 = "blink";
  private static final String TAG_TAG = "tag";

  // ignore tags:
  // TabLayout-TabItem
  private static final String TAG_IGNORE_TAB_ITEM = "com.google.android.material.tabs.TabItem";

  private static final String ATTR_LAYOUT = "layout";

  private final Context mContext;
  private Iterator<View> mViewTreeTempIterator;
  private final Map<View, ThemeAttributeAdapter<? extends View>> mViewThemeAttributeMap = new ArrayMap<>();

  private final ViewThemeAttributeParser mViewThemeAttributeParser = new ViewThemeAttributeParser();

  public LayoutThemeAttributesCollector(Context context) {
    this.mContext = context;
  }

  /**
   * 收集布局中的 View 主题相关的属性信息
   *
   * @param root   布局的根 View
   * @param layout 布局 Id
   */
  public void collectInfo(View root, @LayoutRes int layout) {
    mViewTreeTempIterator = new ViewTreeIterator((ViewGroup) root);
    collectViewsThemeAttributes(layout);
    mViewTreeTempIterator = null;
  }

  /**
   * 替换或添加 View 信息
   *
   * @param view             View
   * @param attributeAdapter View 对应的属性适配器
   * @param <T>              适配器泛型
   */
  public <T extends View> void setInfo(T view, ThemeAttributeAdapter<T> attributeAdapter) {
    mViewThemeAttributeMap.put(view, attributeAdapter);
  }

  public <T extends View> ThemeAttributeAdapter<T> getInfo(T view) {
    // noinspection unchecked: code checked
    return (ThemeAttributeAdapter<T>) mViewThemeAttributeMap.get(view);
  }

  /**
   * 清理保存的 View 主题相关的属性信息
   */
  public void cleanInfo() {
    mViewThemeAttributeMap.clear();
  }

  /**
   * 获取需要换主题的 View 与主题属性的映射表
   *
   * @return 映射表
   */
  public Map<View, ThemeAttributeAdapter<? extends View>> getViewThemeAttributeMap() {
    return mViewThemeAttributeMap;
  }

  private void collectViewsThemeAttributes(@LayoutRes int layout) {
    try (XmlResourceParser parser = mContext.getResources().getLayout(layout)) {
      parseLayoutAndCollectViewsThemeAttributes(parser);
    }
  }

  private void parseLayoutAndCollectViewsThemeAttributes(XmlResourceParser parser) {
    AttributeSet attributeSet = Xml.asAttributeSet(parser);
    try {
      int type;
      // 找到头节点
      // noinspection StatementWithEmptyBody
      while ((type = parser.next()) != XmlPullParser.START_TAG && type != XmlPullParser.END_DOCUMENT) {}

      if (type != XmlResourceParser.START_TAG)
        throw new RuntimeException(parser.getPositionDescription() + ": No start tag found!");

      String name = parser.getName();

      if (TAG_MERGE.equals(name)) {
        TfDebug.logD(TAG, "ignore <merge/> tag");
      } else {
        View view = findNextViewByTagName(parser.getPositionDescription(), name);

        // collect first view's theme entries
        collectViewThemeAttribute(view, attributeSet);
      }

      // collect other view's theme entries
      collectOtherViewsThemeAttributes(parser, attributeSet);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void collectOtherViewsThemeAttributes(XmlResourceParser parser, AttributeSet attributeSet)
      throws IOException, XmlPullParserException {
    final int depth = parser.getDepth();
    // 到达上一个节点的末尾
    int type;
    while (((type = parser.next()) != XmlPullParser.END_TAG || parser.getDepth() > depth) &&
        type != XmlPullParser.END_DOCUMENT) {

      // 找到起始节点
      if (type != XmlPullParser.START_TAG)
        continue;

      String name = parser.getName();
      switch (name) {
      case TAG_INCLUDE:
        TfDebug.logD(TAG, "handle <include/> tag");
        handleIncludeTag(attributeSet);
        break;
      case TAG_IGNORE_TAB_ITEM:
      case TAG_TAG:
      case TAG_REQUEST_FOCUS:
      case TAG_1995:
        TfDebug.logD(TAG, "ignore <" + name + "/> tag");
        break;
      default:
        if (!mViewTreeTempIterator.hasNext())
          throw new RuntimeException(parser.getPositionDescription() + ": Can not find the next " +
              "view in the view tree");

        View view = findNextViewByTagName(parser.getPositionDescription(), name);

        collectViewThemeAttribute(view, attributeSet);
        break;
      }
    }
  }

  private void handleIncludeTag(AttributeSet attributeSet) {
    int layout = attributeSet.getAttributeResourceValue(null, ATTR_LAYOUT, 0);
    if (layout == 0) {
      Log.w(TAG, "layout is invalid");
      return;
    }

    TypedValue outValue = new TypedValue();
    if (mContext.getTheme().resolveAttribute(layout, outValue, true))
      layout = outValue.resourceId;

    if (layout == 0) {
      Log.w(TAG, "layout is invalid");
      return;
    }

    XmlResourceParser childParser = mContext.getResources().getLayout(layout);

    try {
      final AttributeSet childAttributeSet = Xml.asAttributeSet(childParser);
      int type;
      // 找到头节点
      // noinspection StatementWithEmptyBody
      while ((type = childParser.next()) != XmlPullParser.START_TAG && type != XmlPullParser.END_DOCUMENT) {}

      if (type != XmlResourceParser.START_TAG)
        throw new RuntimeException(childParser.getPositionDescription() + ": No start tag found!");

      final String childName = childParser.getName();

      if (TAG_MERGE.equals(childName)) {
        TfDebug.logD(TAG, "ignore <merge/> tag");
      } else {
        // collect first view's theme entries
        View view = findNextViewByTagName(childParser.getPositionDescription(), childName);

        collectViewThemeAttribute(view, childAttributeSet);
      }

      // collect other view's theme entries
      collectOtherViewsThemeAttributes(childParser, childAttributeSet);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // 通过标签名匹配对应
  private View findNextViewByTagName(String positionDesc, String tagName) {
    while (mViewTreeTempIterator.hasNext()) {
      View find = mViewTreeTempIterator.next();

      // for custom views
      String viewTypeName = find.getClass().getName();
      // for framework's views, for example View, Button, ProgressBar...
      String simpleName = find.getClass().getSimpleName();
      // for AppCompatActivity, it will convert the View in the framework to AppCompatXXX
      String fixTagName = "AppCompat" + tagName;
      if (tagName.equals(viewTypeName) || tagName.equals(simpleName) || fixTagName.equals(simpleName))
        return find;
    }

    throw new RuntimeException(positionDesc + ": No view matching tag <" + tagName + "> found");
  }

  private void collectViewThemeAttribute(View view, AttributeSet attributeSet) {
    ThemeAttributeAdapter<? extends View> themeAttributeAdapter = mViewThemeAttributeParser.parse(view, attributeSet);
    if (themeAttributeAdapter != null)
      mViewThemeAttributeMap.put(view, themeAttributeAdapter);
  }
}
