# MultilevelHierarchy

A generic tree representation in Scala 3 whose node contents are identifiable with some scope, useful to work with collection data hierarchy.

### A demo tree:
![A demo user hierarchy](./resources/DemoTree.png)

## The equivalent representation in code: 
### 1. Construction
<code>
        val treeRoot = ResourceTree.construct(root)
        val tree11 = ResourceTree.construct(s11)
        val tree21 = ResourceTree.construct(s21)
        val tree22 = ResourceTree.construct(s22)
        val tree31 = ResourceTree.construct(s31)
        val tree41 = ResourceTree.construct(s41)
</code>
