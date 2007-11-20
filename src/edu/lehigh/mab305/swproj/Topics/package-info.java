/**
 * Contains classes dealing with the storage and representation of
 * Location and Topic domain objects, by storing them in an n-ary
 * tree format with lexically-sorted branches. Controller classes
 * should be used to create the HierarchyTrees; unfortunately, due
 * to certain static castinc constraints, there are two tree data
 * structure sets, HierarchyTree<T> and HierarchyStringTree, where 
 * the latter is simply a specialized version of the former, in
 * which case one particular option requires that a stringified 
 * version of the encapsulated data be operated upon, and Java 
 * generics offers no toString() method for generic types, even 
 * though this is a requirement for all classes inheriting from
 * Object.
 * <p>
 * Actually, I just realized that the declaration: <br/>
 * <code>HierarchyTree<T extends Object></code></br>
 * may be used, which allows for this. I may rewrite this, if I 
 * have time. 
 * 
 * @since 1.0
 * @author Marc Bollinger - mbollinger@gmail.com
 */
package edu.lehigh.mab305.swproj.Topics;