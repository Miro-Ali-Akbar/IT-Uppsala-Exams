#include <stdlib.h>
#include "treemap.h"

typedef struct node node_t;

struct treemap
{
  node_t *root;
};

struct node
{
  int key;
  void *value;
  node_t *left;
  node_t *right;
};

treemap_t *treemap_create()
{
  return calloc(1, sizeof(treemap_t)); 
}

static node_t *node_new(int key, void *value)
{
  node_t *n = calloc(1, sizeof(node_t));
  n->key = key;
  n->value = value;
  return n;
}

static void node_destroy(node_t *n)
{
  /// TODO: frivillig hjälpfunktion

  /// Hint -- use this as a helper function to treemap_destroy
  /// Implement it as a recursive function that first visits
  /// the subtrees and then destroys n 
}

void treemap_destroy(treemap_t *t)
{
  /// TODO: lägg till enligt spec
}

void *treemap_insert(treemap_t *t, int key, void *value)
{
  /// TODO: lägg till enligt spec
  /// Remember there are instructions in the exam text
  /// for how to implement insert 
  
  /// Hint: use a recursive help function (or double pointers)
  return NULL;
}

void *treemap_lookup(treemap_t *t, int key)
{
  /// TODO: lägg till enligt spec
  return NULL;
}

/// Hint: look at this function for inspiration
bool treemap_has_key(treemap_t *t, int key)
{
  node_t *n = t->root;

  while (n)
    {
      if (n->key == key) return true;

      if (n->key > key)
        {
          n = n->left;
        }
      else
        {
          n = n->right;
        }
    }

  /// If we came here without finding it, it can't exist
  return false;
}

/// ===================================================
/// From here on: given functions used by driver 
/// ===================================================

// Help function to treemap_keys
// You do not need to read or change this function
static void node_keys(node_t *n, int **keys)
{
  if (n == NULL) return;

  **keys = n->key;
  ++*keys;
  node_keys(n->left, keys);
  node_keys(n->right, keys);
}

// You do not need to read or look at this function!
int *treemap_keys(treemap_t *t)
{
  int *keys = calloc(treemap_size(t) + 1, sizeof(int));
  int *cursor = keys;
  node_keys(t->root, &cursor);

  return keys;
}

/// Helper function for treemap_size: 
static void node_size(node_t *n, int *acc)
{
  /// Postorder traversal

  /// Recurse into left subtree
  if (n->left != NULL) node_size(n->left, acc);
  /// Recurse into right subtree
  if (n->right != NULL) node_size(n->right, acc);

  /// Increment the accumulator
  *acc += 1;
}

int treemap_size(treemap_t *t)
{
  int result = 0;
  node_size(t->root, &result);
  return result;
}
