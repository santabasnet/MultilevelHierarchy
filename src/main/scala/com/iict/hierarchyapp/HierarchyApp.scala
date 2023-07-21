package com.iict.hierarchyapp

import com.iict.hierarchy.{Id, ResourceTree, Scope}

/**
 * This class is a part of the package com.iict.hierarchyapp and the package
 * is a part of the project MultilevelHierarchy.
 *
 * Integrated ICT Pvt. Ltd. Jwagal, Lalitpur, Nepal.
 * https://www.integratedict.com.np
 * https://www.semantro.com
 *
 * Email: sbasnet81[at]gmail[dot]com
 * Github: https://github.com/santabasnet
 *
 * Created by Santa on 2023-07-21.
 */

/**
 * A demo resource for the node content data.
 *
 * @param id   , given id of the resource content.
 * @param scope, given scope for the resource content.
 */
case class User(id: Id, scope: Scope)

/**
 * Transformation function for the given resource to the Id type.
 */
val userId: Option[User] => Option[Id] = (user: Option[User]) => user.map(_.id)

/**
 * Transformation function for the given resource to the Scope type.
 */
val userScope: Option[User] => Option[Scope] = (user: Option[User]) => user.map(_.scope)

/**
 * A Demo App for the tree usage..
 */
object TreeApp:

    def main(args: Array[String]): Unit = {
        /**
         * Demo.
         */
        println("Tree Hierarchy Representation: ")
        println("---------------------------------------------------------------------\n")
        /**
         * 1. Demo Check.
         */
        println("Validate the Non Empty of Empty Tree: " + ResourceTree.empty.nonEmpty)
        println("---------------------------------------------------------------------\n")


        /**
         * Some Resource Instances.
         */
        val root = User("root", "read")
        val s11 = User("11", "read")
        val s21 = User("21", "write")
        val s22 = User("22", "write")
        val s31 = User("31", "write")
        val s41 = User("41", "read")

        /**
         * Some Resource Tree Instances.
         */
        val treeRoot = ResourceTree.construct(root)
        val tree11 = ResourceTree.construct(s11)
        val tree21 = ResourceTree.construct(s21)
        val tree22 = ResourceTree.construct(s22)
        val tree31 = ResourceTree.construct(s31)
        val tree41 = ResourceTree.construct(s41)

        /**
         *
         * Demo output for the transformation.
         */
        println("\nTree  1: Id = " + treeRoot.identify(userId) + ", Scope = " + treeRoot.scopify(userScope))
        println("Tree 41: Id = " + tree41.identify(userId) + ", Scope = " + tree41.scopify(userScope))
        println("---------------------------------------------------------------------\n")

        /**
         * Tree operations using ++ operations.
         */
        val wholeTree0 = treeRoot ++ (tree11 ++ (tree21 ++ (tree31 ++ (tree41))) ++ tree22)
        val wholeTree1 = treeRoot ++ (tree11 ++ Vector(tree21 ++ (tree31 ++ tree41), tree22))

        /**
         * Tree operations using add sub-tree operation.
         */
        val wholeTree2 =
            treeRoot.addSubtree(
                tree11.addSubtree(
                    tree21.addSubtree(
                        tree31.addSubtree(
                            tree41
                        )
                    )
                ).addSubtree(
                    tree22
                )
            )

        /**
         * Tree operations using add sub-tree with given tree collections.
         */
        val wholeTree3 = ResourceTree.construct(root)
            .addSubtree(
                ResourceTree.construct(s11).addSubtree(
                    Vector(
                        ResourceTree.construct(s21)
                            .addSubtree(ResourceTree.construct(s31)
                                .addSubtree(ResourceTree.construct(s41))),
                        ResourceTree.construct(s22)
                    )))

        /**
         * Demo output, shows the same tree.
         */
        println(wholeTree0)
        println(wholeTree1)
        println(wholeTree2)
        println(wholeTree3)
        println("---------------------------------------------------------------------\n")
    }


