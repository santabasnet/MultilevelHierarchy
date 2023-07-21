package com.iict.hierarchy

/**
 * This class is a part of the package com.iict.hierarchy and the package
 * is a part of the project TypeTest.
 *
 * Integrated ICT Pvt. Ltd. Jwagal, Lalitpur, Nepal.
 * https://www.integratedict.com.np
 * https://www.semantro.com
 *
 * Email: sbasnet81[at]gmail[dot]com
 * Github: https://github.com/santabasnet
 *
 * Created by Santa on 2023-07-13.
 */

/**
 * Target types, it could be your own definition.
 */
type Id = String
type Scope = String

/**
 * A resource representation.
 *
 * @tparam F, higher kinded type, could be useful for some generic transformation.
 * @tparam T, a typed parameter of the tree content.
 */
trait Resource[F[+_], +T]:

    /** A value for the given node tree.
     */
    val value: Option[T]

    /** A children of the given note tree.
     */
    val children: Vector[F[T]]

    /** To add a single children sub tree.
     *
     * @param sibling,
     *               given sibling.
     * @tparam K,
     *          given contravariant type.
     * @return
     * targetTree.
     */
    def addSubtree[K >: T](sibling: F[K]): F[K]

    /**
     * To add multiple child sub trees.
     *
     * @param siblings, given siblings.
     * @tparam K, given contravariant type.
     * @return a targetTree
     */
    def addSubtree[K >: T](siblings: Vector[F[K]]): F[K]

    /** To add children, an interface variant for the child sibling.
     *
     * @param sibling,
     *               given sibling.
     * @tparam K,
     *          given contravariant type.
     * @return
     * targetTree.
     */
    def ++[K >: T](sibling: F[K]): F[K]

    /** To children, an interface variant for the child siblings.
     *
     * @param siblings,
     *                given sibling.
     * @tparam K,
     *          given contravariant type.
     * @return
     * targetTree.
     */
    def ++[K >: T](siblings: Vector[F[K]]): F[K]


    /** Returns the collection of child values.
     *
     * @return
     * childValues.
     */
    def childValues: Vector[Option[T]]

    /** An interface to validate whether the given tree is at leaf place or not.
     *
     * @return
     * true, if it is at the leaf position.
     */
    def isLeaf: Boolean

    /** Validates whether the given tree is of an empty instance or not.
     *
     * @return
     * true, if the tree is empty.
     */
    def isEmpty: Boolean


/** An identifiable type.
 *
 * @tparam T,
 *          given type.
 */
trait Identifiable[+T]:
    def identify(f: Option[T] => Option[Id]): Option[Id]


/** Scopable Type.
 *
 * @tparam T,
 *          given scopable type instance.
 */
trait Scopable[+T]:
    def scopify(f: Option[T] => Option[Scope]): Option[Scope]


/** A resource tree representation.
 *
 * @param value
 *                , the value for the node.
 * @param children,
 *                the children representation.
 * @tparam T,
 *          given type.
 */
case class ResourceTree[+T](
                               override val value: Option[T],
                               override val children: Vector[ResourceTree[T]]
                           ) extends Identifiable[T]
    with Scopable[T]
    with Resource[ResourceTree, T]:

    /** Identify the given type, maps to Id.
     *
     * @param f,
     *         a transformation function type.
     * @return
     * an Id.
     */
    override def identify(f: Option[T] => Option[Id]): Option[Id] = f(value)

    /** A scopable given type.
     *
     * @param f,
     *         a transformation function.
     * @return
     * a Scope type.
     */
    override def scopify(f: Option[T] => Option[Scope]): Option[Scope] = f(value)

    /** Formats the children to the sequence of type T.
     *
     * @return
     * vector of T.
     */
    def childValues: Vector[Option[T]] = children.map(_.value).filter(_.isDefined)

    /** An interface to validate whether the given tree is at leaf place or not.
     *
     * @return
     * true, if it is at the leaf position.
     */
    override def isLeaf: Boolean = this.children.isEmpty

    /** An interface to add a children.
     *
     * @tparam K,
     *          contravariant of R.
     * @return
     * a new resource tree.
     */
    override def addSubtree[K >: T](sibling: ResourceTree[K]): ResourceTree[K] =
        ResourceTree[K](this.value, this.children :+ sibling)

    /**
     * To add multiple child sub trees.
     *
     * @param siblings, given siblings.
     * @tparam K, given contravariant type.
     * @return a targetTree
     */
    def addSubtree[K >: T](siblings: Vector[ResourceTree[K]]): ResourceTree[K] =
        ResourceTree[K](this.value, this.children ++ siblings)

    /** An interface to to add child with operator notation.
     *
     * @param sibling,
     *               a tree sibling.
     * @tparam K,
     *          given type parameter.
     * @return
     * a new resource tree.
     */
    override def ++[K >: T](sibling: ResourceTree[K]): ResourceTree[K] =
        addSubtree(sibling)

    /** An interface to to add child with operator notation.
     *
     * @param siblings,
     *                a tree sibling.
     * @tparam K,
     *          given type parameter.
     * @return
     * a new resource tree.
     */
    override def ++[K >: T](siblings: Vector[ResourceTree[K]]): ResourceTree[K] =
        addSubtree(siblings)

    /** Validates whether the given tree is of an empty instance or not.
     *
     * @return
     * true, if the tree is empty.
     */
    def isEmpty: Boolean = this.value.isEmpty && this.children.isEmpty

    /** Validates if the tree is of non-empty type or not.
     *
     * @return
     * true, if the tree is non-empty instance.
     */
    def nonEmpty: Boolean = this.value.nonEmpty



/** The companion object of the resource tree, used for factory purpose.
 */
object ResourceTree:

    /** The utility of the empty children of type T.
     */
    private def emptyChildren[T]: Vector[ResourceTree[T]] = Vector[ResourceTree[T]]()

    /** An empty instance of the
     *
     * @return
     * an empty instance of the resource tree.
     */
    def empty: ResourceTree[Nothing] = ResourceTree(None, emptyChildren[Nothing])

    /** Build a resource tree with a value given of some type T.
     */
    def construct[T](value: Option[T]): ResourceTree[T] = ResourceTree(value, emptyChildren[T])

    /** Build a resource tree with a value given of type T.
     */
    def construct[T](value: T): ResourceTree[T] = construct[T](Some(value))

    /**
     * Build a resource tree with a given value and the collection of subtrees.
     */
    def construct[T](value: T, children: ResourceTree[T]): ResourceTree[T] =
        ResourceTree.construct[T](value).addSubtree(children)

    /**
     * Build a resource tree with a given value and the collection of subtrees.
     */
    def construct[T](value: T, children: Vector[ResourceTree[T]]): ResourceTree[T] =
        ResourceTree.construct[T](value).addSubtree(children)



