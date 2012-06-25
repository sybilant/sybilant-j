/**
 * Copyright © 2012 Paul Stadig. All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * This Source Code Form is "Incompatible With Secondary Licenses", as
 * defined by the Mozilla Public License, v. 2.0.
 */
package sybilant;

public class PersistentDeque<T> implements Countable, Deque<T> {
  static enum Color {
    RED, YELLOW, GREEN
  }

  static class Node<T> {
    static final Node<?> Empty = new Node<>();

    @SuppressWarnings("unchecked")
    static <T> Node<T> empty() {
      return (Node<T>) Empty;
    }

    private static Color arrayColor(final Array<?> array) {
      switch (array.count()) {
      case 0:
        return Color.RED;
      case 1:
        return Color.YELLOW;
      case YELLOW_LIMIT:
        return Color.YELLOW;
      case RED_LIMIT:
        return Color.RED;
      default:
        return Color.GREEN;
      }
    }

    @SuppressWarnings("unchecked")
    private static <T> Node<T> regularize(final Node<T> n) {
      Node<T> node = n;
      if (node.isEmpty()
          || node.isGreen()
          || node.isYellow(true)
          && (node.child.isEmpty() || node.child.isGreen() || node.child
              .isYellow(node.substack.isEmpty())
              && (node.substack.isEmpty() || node.substack.isGreen()))) {
        return node;
      }
      assert node.isRed() || node.child.isRed() || node.substack.isRed();
      if (!node.child.isEmpty() && node.child.isRed()) {
        return new Node<>(node.prefix, regularize(node.child), node.substack,
            node.suffix);
      }
      if (!node.substack.isEmpty() && node.substack.isRed()) {
        return new Node<>(node.prefix, node.child, regularize(node.substack),
            node.suffix);
      }
      Array<T> Pi = node.prefix;
      Array<Array<T>> Pi1 = node.child.prefix;
      Array<Array<T>> Si1 = node.child.suffix;
      Array<T> Si = node.suffix;
      assert !Pi1.isEmpty() && !Si1.isEmpty() || node.child.child.isEmpty() : "level i + 1 may not be red";
      if (Pi1.count() + Si1.count() >= 2) {
        // assert false : "two buffer case";
        if (Pi1.isEmpty()) {
          Pi1 = Pi1.inject(Si1.first());
          Si1 = Si1.pop();
        }
        if (Si1.isEmpty()) {
          Si1 = Si1.inject(Pi1.last());
          Pi1 = Pi1.eject();
        }
        if (Pi.count() >= YELLOW_LIMIT) {
          Pi1 = Pi1.push(Pi.slice(Pi.count() - 2, Pi.count()));
          Pi = Pi.slice(0, Pi.count() - 2);
        }
        if (Si.count() >= YELLOW_LIMIT) {
          Si1 = Si1.inject(Si.slice(0, 2));
          Si = Si.slice(2, Si.count());
        }
        if (Pi.count() <= 1) {
          Pi = Pi.inject(Pi1.first().first()).inject(Pi1.first().last());
          Pi1 = Pi1.pop();
        }
        if (Si.count() <= 1) {
          Si = Si.push(Si1.last().last()).push(Si1.last().first());
          Si1 = Si1.eject();
        }
        Node<Array<T>> child = empty();
        if (!Pi1.isEmpty() || !Si1.isEmpty()) {
          child = new Node<>(Pi1, node.child.child, node.child.substack, Si1);
        }
        node = new Node<>(Pi, child, node.substack, Si);
      } else if (Pi1.count() + Si1.count() <= 1
          && (Pi.count() >= 2 || Si.count() >= 2)) {
        // assert false : "one buffer case";
        if (Si1.count() == 1) {
          Pi1 = Pi1.inject(Si1.first());
          Si1 = Si1.pop();
        }
        if (Pi.count() >= YELLOW_LIMIT) {
          Pi1 = Pi1.push(Pi.slice(Pi.count() - 2, Pi.count()));
          Pi = Pi.slice(0, Pi.count() - 2);
        }
        if (Si.count() >= YELLOW_LIMIT) {
          Pi1 = Pi1.inject(Si.slice(0, 2));
          Si = Si.slice(2, Si.count());
        }
        if (Pi.count() <= 1) {
          Pi = Pi.inject(Pi1.first().first()).inject(Pi1.first().last());
          Pi1 = Pi1.pop();
        }
        if (Si.count() <= 1) {
          Si = Si.push(Pi1.last().last()).push(Pi1.last().first());
          Pi1 = Pi1.eject();
        }
        Node<Array<T>> child = empty();
        if (!Pi1.isEmpty()) {
          child = new Node<>(Pi1, Node.<Array<Array<T>>> empty(), empty(), Si1);
        }
        node = new Node<>(Pi, child, node.substack, Si);
      } else if (Pi1.count() + Si1.count() <= 1 && Pi.count() <= 1
          && Si.count() <= 1) {
        if (Pi1.count() == 1) {
          Pi = Pi.inject(Pi1.first().first()).inject(Pi1.first().last());
        }
        if (Si1.count() == 1) {
          Pi = Pi.inject(Si1.first().first()).inject(Si1.first().last());
        }
        if (Si.count() == 1) {
          Pi = Pi.inject(Si.first());
        }
        node = new Node<>(Pi, Node.<Array<T>> empty(), empty(),
            Array.<T> create());
      } else {
        assert false : "null case";
      }
      if (!node.child.isEmpty()) {
        if (node.substack.isEmpty() && node.child.isYellow(true)) {
          if (node.child.substack.isEmpty() && !node.child.child.isEmpty()
              && !node.child.child.isYellow(true)) {
            final Node<Array<T>> child = new Node<>(node.child.prefix,
                Node.<Array<Array<T>>> empty(), empty(), node.child.suffix);
            assert child.isYellow(node.substack.isEmpty());
            node = new Node<>(node.prefix, child, node.child.child, node.suffix);
          } else if (!node.child.substack.isEmpty()) {
            final Node<Array<T>> child = new Node<>(node.child.prefix,
                node.child.child, empty(), node.child.suffix);
            assert child.isYellow(node.substack.isEmpty());
            node = new Node<>(node.prefix, child, node.child.substack,
                node.suffix);
          }
        } else if (!node.substack.isEmpty() && !node.child.isYellow(false)) {
          assert node.child.substack.isEmpty();
          final Node<Array<T>> child;
          if (node.child.child.isEmpty()) {
            child = new Node<>(node.child.prefix,
                (Node<Array<Array<T>>>) node.substack, empty(),
                node.child.suffix);
          } else {
            child = new Node<>(node.child.prefix, node.child.child,
                node.substack, node.child.suffix);
          }
          node = new Node<>(node.prefix, child, empty(), node.suffix);
        }
      }
      assert node.isRegular();
      return node;
    }

    final Array<T> prefix;
    final Node<Array<T>> child;
    final Node<?> substack;
    final Array<T> suffix;

    @SuppressWarnings("unchecked")
    Node() {
      this.prefix = Array.create();
      this.child = (Node<Array<T>>) this;
      this.substack = this;
      this.suffix = Array.create();
    }

    Node(final Array<T> prefix, final Node<Array<T>> yellows,
        final Node<?> substack, final Array<T> suffix) {
      this.prefix = prefix;
      this.child = yellows;
      this.substack = substack;
      this.suffix = suffix;
      assert invariant();
    }

    @Override
    public String toString() {
      return this.prefix.toString() + " | " + this.suffix.toString();
    }

    T at(final int index) {
      return at(index, empty());
    }

    @SuppressWarnings("unchecked")
    T at(final int index, final Node<?> more) {
      int i = index;
      if (i < this.prefix.count()) {
        return this.prefix.at(i);
      }
      i -= this.prefix.count();
      if (!this.child.isEmpty()) {
        final Node<?> substack = this.substack.isEmpty() ? more : this.substack;
        final int childCount = 2 * this.child.count(substack.count());
        if (i < childCount) {
          final T t = this.child.at(i / 2, substack).at(i % 2);
          if (t != null) {
            return t;
          }
        }
        i -= childCount;
      } else if (!more.isEmpty()) {
        final int childCount = 2 * more.count();
        if (i < childCount) {
          final T t = ((Node<Array<T>>) more).at(i / 2).at(i % 2);
          if (t != null) {
            return t;
          }
        }
        i -= childCount;
      }
      if (i < this.suffix.count()) {
        return this.suffix.at(i);
      }
      return null;
    }

    int count() {
      if (isEmpty()) {
        return 0;
      }
      return count(0);
    }

    int count(final int substackCount) {
      int count = substackCount;
      if (!this.substack.isEmpty()) {
        count = this.substack.count(count);
      }
      if (!this.child.isEmpty()) {
        count = this.child.count(count);
      }
      return 2 * count + this.prefix.count() + this.suffix.count();
    }

    Node<T> eject() {
      if (this.suffix.isEmpty() && this.child.isEmpty()
          && this.substack.isEmpty()) {
        return setPrefix(this.prefix.eject());
      }
      return setSuffix(this.suffix.eject());
    }

    T first() {
      if (this.prefix.isEmpty() && this.child.isEmpty()
          && this.substack.isEmpty()) {
        return this.suffix.first();
      }
      return this.prefix.first();
    }

    Node<T> inject(final T last) {
      if (this.suffix.isEmpty() && this.child.isEmpty()
          && this.substack.isEmpty()) {
        return setPrefix(this.prefix.inject(last));
      }
      return setSuffix(this.suffix.inject(last));
    }

    boolean isEmpty() {
      return this == Empty;
    }

    boolean isRegular() {
      if (isEmpty()) {
        return true;
      }
      assert isGreen()
          || isYellow(true)
          && (this.child.isEmpty() || this.child.isGreen() || this.child
              .isYellow(this.substack.isEmpty())
              && (this.substack.isEmpty() || this.substack.isGreen())) : "the topmost non-yellow node must be green";
      assert isSemiregular(true);
      return true;
    }

    boolean isSemiregular(final boolean bottom) {
      if (isEmpty()) {
        return true;
      }
      assert this.substack.isEmpty() || !this.child.isEmpty()
          && this.child.isYellow(false) : "if a node's substack is not empty, then its child must be yellow";
      assert this.substack.isEmpty() || this.substack.isGreen()
          || this.substack.isRed() : "if a node's substack is not empty, then its substack must be red or green";
      assert !isRed() || this.child.isEmpty() || this.child.isGreen()
          || this.child.isYellow(bottom && this.substack.isEmpty())
          && (this.substack.isEmpty() || this.substack.isGreen()) : "if a node is red, then its child/substack must be empty or green";
      assert this.child.isEmpty()
          || !this.child.isYellow(bottom && this.substack.isEmpty())
          || this.child.child.isEmpty()
          || this.child.child.isYellow(bottom && this.child.substack.isEmpty()) : "if a node is yellow, then its child must be yellow";
      assert this.child.isSemiregular(bottom && this.substack.isEmpty());
      assert this.substack.isSemiregular(true);
      return true;
    }

    T last() {
      if (this.suffix.isEmpty() && this.child.isEmpty()
          && this.substack.isEmpty()) {
        return this.prefix.last();
      }
      return this.suffix.last();
    }

    Node<T> pop() {
      if (this.prefix.isEmpty() && this.child.isEmpty()
          && this.substack.isEmpty()) {
        return setSuffix(this.suffix.pop());
      }
      return setPrefix(this.prefix.pop());
    }

    Node<T> push(final T first) {
      if (this.prefix.isEmpty() && this.child.isEmpty()
          && this.substack.isEmpty()) {
        return setSuffix(this.suffix.push(first));
      }
      return setPrefix(this.prefix.push(first));
    }

    private Color color(final boolean bottom) {
      assert !isEmpty();
      final Color prefixColor = prefixColor();
      final Color suffixColor = suffixColor();
      if (bottom && this.child.isEmpty() && this.substack.isEmpty()
          && this.prefix.isEmpty() ^ this.suffix.isEmpty()) {
        if (!this.prefix.isEmpty()) {
          return prefixColor;
        }
        return suffixColor;
      }
      return prefixColor.compareTo(suffixColor) < 0 ? prefixColor : suffixColor;
    }

    private boolean invariant() {
      if (!isEmpty()) {
        assert this.prefix.count() > 0 || this.suffix.count() > 0;
        assert this.prefix.count() <= RED_LIMIT;
        assert this.suffix.count() <= RED_LIMIT;
        assert this.substack.isEmpty() || !this.child.isEmpty() : "if a node's substack is not empty, then its child must not be empty";
      }
      return true;
    }

    private boolean isGreen() {
      return Color.GREEN.equals(color(true));
    }

    private boolean isRed() {
      return Color.RED.equals(color(true));
    }

    private boolean isYellow(final boolean bottom) {
      return Color.YELLOW.equals(color(bottom));
    }

    private Color prefixColor() {
      return arrayColor(this.prefix);
    }

    private Node<T> setPrefix(final Array<T> prefix) {
      if (prefix.isEmpty() && this.child.isEmpty() && this.substack.isEmpty()
          && this.suffix.isEmpty()) {
        return empty();
      }
      return regularize(new Node<>(prefix, this.child, this.substack,
          this.suffix));
    }

    private Node<T> setSuffix(final Array<T> suffix) {
      if (this.prefix.isEmpty() && this.child.isEmpty()
          && this.substack.isEmpty() && suffix.isEmpty()) {
        return empty();
      }
      return regularize(new Node<>(this.prefix, this.child, this.substack,
          suffix));
    }

    private Color suffixColor() {
      return arrayColor(this.suffix);
    }
  }

  static final int RED_LIMIT = 32;
  static final int YELLOW_LIMIT = RED_LIMIT - 1;
  static final PersistentDeque<?> Empty = new PersistentDeque<>();

  @SafeVarargs
  @SuppressWarnings("unchecked")
  public static <T> PersistentDeque<T> create(final T... values) {
    PersistentDeque<T> deque = (PersistentDeque<T>) Empty;
    if (values != null) {
      for (final T value : values) {
        deque = deque.inject(value);
      }
    }
    return deque;
  }

  final Node<T> root;

  PersistentDeque() {
    this.root = Node.empty();
  }

  PersistentDeque(final Node<T> root) {
    this.root = root;
    assert invariant();
  }

  public T at(final int index) {
    return this.root.at(index);
  }

  @Override
  public int count() {
    return this.root.count();
  }

  @Override
  public PersistentDeque<T> eject() {
    final Node<T> newRoot = this.root.eject();
    if (newRoot.isEmpty()) {
      return empty();
    }
    return new PersistentDeque<>(newRoot);
  }

  @SuppressWarnings("unchecked")
  public PersistentDeque<T> empty() {
    return (PersistentDeque<T>) Empty;
  }

  @Override
  public boolean equals(final Object obj) {
    if (!(obj instanceof Seq<?>)) {
      return false;
    }
    return Seqs.equals(this, (Seq<?>) obj);
  }

  @Override
  public T first() {
    return this.root.first();
  }

  @Override
  public int hashCode() {
    return Seqs.hashCode(this);
  }

  @Override
  public PersistentDeque<T> inject(final T last) {
    return new PersistentDeque<>(this.root.inject(last));
  }

  @Override
  public boolean isEmpty() {
    return this == Empty;
  }

  @Override
  public java.util.Iterator<T> iterator() {
    return new Iterator<>(this);
  }

  @Override
  public T last() {
    return this.root.last();
  }

  @Override
  public PersistentDeque<T> pop() {
    final Node<T> newRoot = this.root.pop();
    if (newRoot.isEmpty()) {
      return empty();
    }
    return new PersistentDeque<>(newRoot);
  }

  @Override
  public PersistentDeque<T> push(final T first) {
    return new PersistentDeque<>(this.root.push(first));
  }

  @Override
  public PersistentDeque<T> seq() {
    return this;
  }

  @Override
  public String toString() {
    return Seqs.toString(this);
  }

  private boolean invariant() {
    assert this == Empty || !this.root.isEmpty();
    assert this.root.isRegular();
    return true;
  }
}
