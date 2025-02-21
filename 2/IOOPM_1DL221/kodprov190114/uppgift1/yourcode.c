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
  if (!n) 
    return;

  node_destroy(n->left);
  node_destroy(n->right);

  free(n);
}

void treemap_destroy(treemap_t *t)
{
  if (!t->root) {
    free(t);
  }
  else {
    node_destroy(t->root->left);
    node_destroy(t->root->right);

    free(t->root);
    free(t);
  } 
}

void *insert_subtree(node_t **subtree, int key, void *value){

  if (!*subtree) {
    *subtree = node_new(key, value);
    return NULL;
  }

  if (key == (*subtree)->key) {
    void *return_value = (*subtree)->value;
    (*subtree)->value = value;
    return return_value;
  }
  else if (key < (*subtree)->key) {
    return insert_subtree(&(*subtree)->left, key, value);
  }
  else {
    return insert_subtree(&(*subtree)->right, key, value);
  }
}


void *treemap_insert(treemap_t *t, int key, void *value)
{
  if (!t->root) {
    t->root = node_new(key, value);
    return NULL;
  }

  if (t->root->key == key) {
    void *ret = t->root->value;
    t->root->value = value;
    return ret;
  }
  if (key < t->root->key ) {
    return insert_subtree(&t->root, key, value);
  }
  else {
    return insert_subtree(&t->root, key, value);
  }
}

void *lookup_subtree(node_t *subtree, int key){

  if (!subtree) 
    return subtree;
  

  if (key == subtree->key)
    return subtree->value;

  if (key < subtree->key) 
    return lookup_subtree(subtree->left, key);
  else 
    return lookup_subtree(subtree->right, key);

}

void *treemap_lookup(treemap_t *t, int key)
{

  if (!t->root)
    return NULL;
  
  if (t->root->key == key)
    return t->root->value;

  if (key < t->root->key)
    return lookup_subtree(t->root->left, key);
  else
    return lookup_subtree(t->root->right, key);
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
  //if (!n)
  //return;
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
  if (t->root) 
    node_size(t->root, &result);
  return result;
}
