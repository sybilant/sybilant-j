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

    private static Color arrayColor(final ArrayVector<?> array) {
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

    private static Color color(final boolean bottom, final ArrayVector<?> prefix,
        final ArrayVector<?> suffix) {
      assert !bottom || !prefix.isEmpty() || !suffix.isEmpty();
      final Color prefixColor = arrayColor(prefix);
      final Color suffixColor = arrayColor(suffix);
      if (bottom && prefix.isEmpty() ^ suffix.isEmpty()) {
        if (!prefix.isEmpty()) {
          return prefixColor;
        }
        return suffixColor;
      }
      return prefixColor.compareTo(suffixColor) < 0 ? prefixColor : suffixColor;
    }

    private static boolean isNodeGreen(final boolean bottom,
        final ArrayVector<?> prefix, final ArrayVector<?> suffix) {
      return Color.GREEN.equals(color(bottom, prefix, suffix));
    }

    private static boolean isNodeRed(final boolean bottom,
        final ArrayVector<?> prefix, final ArrayVector<?> suffix) {
      return Color.RED.equals(color(bottom, prefix, suffix));
    }

    private static boolean isNodeYellow(final boolean bottom,
        final ArrayVector<?> prefix, final ArrayVector<?> suffix) {
      return Color.YELLOW.equals(color(bottom, prefix, suffix));
    }

    @SuppressWarnings("unchecked")
    private static <T> Node<T> regularize(final ArrayVector<T> prefix,
        final Node<ArrayVector<T>> child, final Node<?> substack,
        final ArrayVector<T> suffix) {
      ArrayVector<T> Pi = prefix;
      ArrayVector<ArrayVector<T>> Pi1 = child.prefix;
      ArrayVector<ArrayVector<T>> Si1 = child.suffix;
      ArrayVector<T> Si = suffix;
      assert !Pi1.isEmpty() && !Si1.isEmpty() || child.child.isEmpty() : "level i + 1 may not be red";
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
          final Object[] values = new Object[Pi.count() + 2];
          System.arraycopy(Pi.values, 0, values, 0, Pi.count());
          System.arraycopy(Pi1.first().values, 0, values, Pi.count(), 2);
          Pi = new ArrayVector<>(values);
          Pi1 = Pi1.pop();
        }
        if (Si.count() <= 1) {
          final Object[] values = new Object[Si.count() + 2];
          System.arraycopy(Si1.last().values, 0, values, 0, 2);
          System.arraycopy(Si.values, 0, values, 2, Si.count());
          Si = new ArrayVector<>(values);
          Si1 = Si1.eject();
        }
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
          final Object[] values = new Object[Pi.count() + 2];
          System.arraycopy(Pi.values, 0, values, 0, Pi.count());
          System.arraycopy(Pi1.first().values, 0, values, Pi.count(), 2);
          Pi = new ArrayVector<>(values);
          Pi1 = Pi1.pop();
        }
        if (Si.count() <= 1) {
          final Object[] values = new Object[Si.count() + 2];
          System.arraycopy(Pi1.last().values, 0, values, 0, 2);
          System.arraycopy(Si.values, 0, values, 2, Si.count());
          Si = new ArrayVector<>(values);
          Pi1 = Pi1.eject();
        }
      } else if (Pi1.count() + Si1.count() <= 1 && Pi.count() <= 1
          && Si.count() <= 1) {
        if (Pi1.count() == 1) {
          final Object[] values = new Object[Pi.count() + 2];
          System.arraycopy(Pi.values, 0, values, 0, Pi.count());
          System.arraycopy(Pi1.first().values, 0, values, Pi.count(), 2);
          Pi = new ArrayVector<>(values);
          Pi1 = Pi1.pop();
        }
        if (Si1.count() == 1) {
          final Object[] values = new Object[Pi.count() + 2];
          System.arraycopy(Pi.values, 0, values, 0, Pi.count());
          System.arraycopy(Si1.first().values, 0, values, Pi.count(), 2);
          Pi = new ArrayVector<>(values);
          Si1 = Si1.pop();
        }
        if (Si.count() == 1) {
          Pi = Pi.inject(Si.first());
          Si = Si.pop();
        }
      } else {
        assert false : "null case";
      }
      if (!Pi1.isEmpty() || !Si1.isEmpty()) {
        final boolean childIsYellow = isNodeYellow(substack.isEmpty()
            && child.child.isEmpty() && child.substack.isEmpty(), Pi1, Si1);
        if (substack.isEmpty() && childIsYellow) {
          if (child.substack.isEmpty() && !child.child.isEmpty()
              && !child.isChildYellow(true)) {
            final Node<ArrayVector<T>> newChild = new Node<>(Pi1,
                Node.<ArrayVector<ArrayVector<T>>> empty(), empty(), Si1);
            assert newChild.isYellow(substack.isEmpty());
            final Node<T> node = new Node<>(Pi, newChild, child.child, Si);
            assert node.isRegular();
            return node;
          } else if (!child.substack.isEmpty()) {
            final Node<ArrayVector<T>> newChild = new Node<>(Pi1, child.child,
                empty(), Si1);
            assert newChild.isYellow(substack.isEmpty());
            final Node<T> node = new Node<>(Pi, newChild, child.substack, Si);
            assert node.isRegular();
            return node;
          } else {
            final Node<T> node = new Node<>(Pi, new Node<>(Pi1, child.child,
                child.substack, Si1), substack, Si);
            assert node.isRegular();
            return node;
          }
        } else if (!substack.isEmpty() && !childIsYellow) {
          assert child.substack.isEmpty();
          Node<ArrayVector<T>> newChild;
          if (child.child.isEmpty()) {
            newChild = new Node<>(Pi1, (Node<ArrayVector<ArrayVector<T>>>) substack,
                empty(), Si1);
          } else {
            newChild = new Node<>(Pi1, child.child, substack, Si1);
          }
          final Node<T> node = new Node<>(Pi, newChild, empty(), Si);
          assert node.isRegular();
          return node;
        } else {
          final Node<T> node = new Node<>(Pi, new Node<>(Pi1, child.child,
              child.substack, Si1), substack, Si);
          assert node.isRegular();
          return node;
        }
      }
      final Node<T> node = new Node<>(Pi, Node.<ArrayVector<T>> empty(), empty(), Si);
      assert node.isRegular();
      return node;
    }

    final ArrayVector<T> prefix;
    final Node<ArrayVector<T>> child;
    final Node<?> substack;
    final ArrayVector<T> suffix;

    @SuppressWarnings("unchecked")
    Node() {
      this.prefix = ArrayVector.create();
      this.child = (Node<ArrayVector<T>>) this;
      this.substack = this;
      this.suffix = ArrayVector.create();
    }

    Node(final ArrayVector<T> prefix, final Node<ArrayVector<T>> yellows,
        final Node<?> substack, final ArrayVector<T> suffix) {
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
          final T t = ((Node<ArrayVector<T>>) more).at(i / 2).at(i % 2);
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
          && (this.child.isEmpty() || this.child.isGreen() || this
              .isChildYellow(true)
              && (this.substack.isEmpty() || this.substack.isGreen())) : "the topmost non-yellow node must be green";
      assert isSemiregular(true);
      return true;
    }

    boolean isSemiregular(final boolean bottom) {
      if (isEmpty()) {
        return true;
      }
      assert this.substack.isEmpty() || !this.child.isEmpty()
          && this.isChildYellow(false) : "if a node's substack is not empty, then its child must be yellow";
      assert this.substack.isEmpty() || this.substack.isGreen()
          || this.substack.isRed() : "if a node's substack is not empty, then its substack must be red or green";
      assert !isRed() || this.child.isEmpty() || this.child.isGreen()
          || this.isChildYellow(bottom)
          && (this.substack.isEmpty() || this.substack.isGreen()) : "if a node is red, then its child/substack must be empty or green";
      assert this.child.isEmpty() || !this.isChildYellow(bottom)
          || this.child.child.isEmpty() || this.child.isChildYellow(bottom) : "if a node is yellow, then its child must be yellow";
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

    private boolean invariant() {
      if (!isEmpty()) {
        assert this.prefix.count() > 0 || this.suffix.count() > 0;
        assert this.prefix.count() <= RED_LIMIT;
        assert this.suffix.count() <= RED_LIMIT;
        assert this.substack.isEmpty() || !this.child.isEmpty() : "if a node's substack is not empty, then its child must not be empty";
      }
      return true;
    }

    private boolean isChildYellow(final boolean bottom) {
      return isNodeYellow(
          bottom && this.substack.isEmpty() && this.child.child.isEmpty()
              && this.child.substack.isEmpty(), this.child.prefix,
          this.child.suffix);
    }

    private boolean isGreen() {
      return isNodeGreen(this.child.isEmpty() && this.substack.isEmpty(),
          this.prefix, this.suffix);
    }

    private boolean isRed() {
      return isNodeRed(this.child.isEmpty() && this.substack.isEmpty(),
          this.prefix, this.suffix);
    }

    private boolean isYellow(final boolean bottom) {
      return isNodeYellow(
          bottom && this.child.isEmpty() && this.substack.isEmpty(),
          this.prefix, this.suffix);
    }

    @SuppressWarnings("unchecked")
    private Node<T> setPrefix(final ArrayVector<T> prefix) {
      if (prefix.isEmpty() && this.child.isEmpty() && this.substack.isEmpty()
          && this.suffix.isEmpty()) {
        return empty();
      }
      if (!this.child.isEmpty() && this.child.isRed()) {
        assert this.substack.isEmpty() : "if a node's child is not yellow, then its substack must be empty";
        return new Node<>(prefix, regularize(this.child.prefix,
            this.child.child, this.child.substack, this.child.suffix),
            this.substack, this.suffix);
      }
      if (!this.substack.isEmpty() && this.substack.isRed()) {
        final Node<T> substack = (Node<T>) this.substack;
        return new Node<>(prefix, this.child, regularize(substack.prefix,
            substack.child, substack.substack, substack.suffix), this.suffix);
      }
      if (isNodeRed(this.child.isEmpty() && this.substack.isEmpty(), prefix,
          this.suffix)) {
        return regularize(prefix, this.child, this.substack, this.suffix);
      }
      return new Node<>(prefix, this.child, this.substack, this.suffix);
    }

    @SuppressWarnings("unchecked")
    private Node<T> setSuffix(final ArrayVector<T> suffix) {
      if (this.prefix.isEmpty() && this.child.isEmpty()
          && this.substack.isEmpty() && suffix.isEmpty()) {
        return empty();
      }
      if (!this.child.isEmpty() && this.child.isRed()) {
        assert this.substack.isEmpty() : "if a node's child is not yellow, then its substack must be empty";
        return new Node<>(this.prefix, regularize(this.child.prefix,
            this.child.child, this.child.substack, this.child.suffix),
            this.substack, suffix);
      }
      if (!this.substack.isEmpty() && this.substack.isRed()) {
        final Node<T> substack = (Node<T>) this.substack;
        return new Node<>(this.prefix, this.child, regularize(substack.prefix,
            substack.child, substack.substack, substack.suffix), suffix);
      }
      if (isNodeRed(this.child.isEmpty() && this.substack.isEmpty(),
          this.prefix, suffix)) {
        return regularize(this.prefix, this.child, this.substack, suffix);
      }
      return new Node<>(this.prefix, this.child, this.substack, suffix);
    }
  }

  static final int RED_LIMIT = 31;
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
