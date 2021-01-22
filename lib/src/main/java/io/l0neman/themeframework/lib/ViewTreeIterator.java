package io.l0neman.themeframework.lib;

import android.view.View;
import android.view.ViewGroup;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/*
         0
      / / \ \
     1  2  3  4
       /\    / \
     5  6   7   8
           /\
          9  10

-> 0 1 5 6 2 3 4 7 9 10 8

直接对 ViewTree 进行中序遍历，改进了之前将所有 View 收集到 List 中再遍历的低效率方法
 */
public class ViewTreeIterator implements Iterator<View> {

  private final ViewGroup root;
  private final LinkedList<Node> nodeStack = new LinkedList<>();

  public ViewTreeIterator(ViewGroup root) {
    this.root = root;
    nodeStack.push(new Node(root, -1));
  }

  private static final class Node {
    View view;
    int pos;

    public Node(View view, int pos) {
      this.view = view;
      this.pos = pos;
    }
  }

  @Override
  public boolean hasNext() {
    return !nodeStack.isEmpty();
  }

  @Override
  public View next() {
    if (!hasNext())
      throw new NoSuchElementException("no next");

    Node peek = nodeStack.peek();
    if (peek.pos == -1) {
      peek.pos++;

      if (isLeafNode(root))
        nodeStack.pop();

      return peek.view;
    }

    ViewGroup viewGroup = (ViewGroup) peek.view;
    if (peek.pos == viewGroup.getChildCount())
      throw new NoSuchElementException("no next");

    View child = viewGroup.getChildAt(peek.pos);
    peek.pos++;
    if (!isLeafNode(child)) {
      nodeStack.push(new Node(child, 0));
    } else while (peek.pos == ((ViewGroup) peek.view).getChildCount()) {
      /*
            0
           / \
          1   2
             / \
            3   4  <- 处理这种情况，最后一个节点，将清空栈
       */
      nodeStack.pop();
      if (!nodeStack.isEmpty())
        peek = nodeStack.peek();
      else break;
    }

    return child;
  }

  private static boolean isLeafNode(View view) {
    return !(view instanceof ViewGroup) || ((ViewGroup) view).getChildCount() == 0;
  }
}
