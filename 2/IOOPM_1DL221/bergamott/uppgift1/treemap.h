#ifndef __treemap_h__
#define __treemap_h__

#include <stdbool.h>

typedef struct treemap treemap_t;

/// Creates and returns a pointer to a new empty treemap.
treemap_t *treemap_create();

/// Removes the treemap from memory, but does not call
/// free on any value pointer passed to it.
void treemap_destroy(treemap_t *);

/// Insert value v under key k in the tree -- if there is
/// already a value v' with key k, return v' and replace
/// v' with v in the tree.
void *treemap_insert(treemap_t *t, int k, void *v);

/// Return the value associated with key k in the tree.
/// If key k is not in the tree, return NULL. 
void *treemap_lookup(treemap_t *t, int k);

/// Returns the true if ther is value associated with the key k in
/// the tree, else false.
bool treemap_has_key(treemap_t *t, int k);

/// Returns an array of integers with all the keys in the
/// treemap in preorder, i.e., the forllowing tree returns
/// [5,2,1,4,8,6,32]. 
/// 
///   treemap_t                  .
///      |                       .
///     _5_                      .
///    /   \                     .
///   2     8                    .
///  / \   / \                   .
/// 1   4 6   32 
int *treemap_keys(treemap_t *t);

/// Returns the number of (key, value) pairs in the tree.
/// The empty tree has size 0. The following tree has size 7.
/// 
///   treemap_t                  .
///      |                       .
///     _5_                      .
///    /   \                     .
///   2     8                    .
///  / \   / \                   .
/// 1   4 6   32 
int treemap_size(treemap_t *t);

#endif
