#define XOPEN_SOURCE 500

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include "treemap.h"

#define assert_eq(got, expected)                                        \
  do {                                                                  \
    int _b_ = (got); int _a_ = (expected);                              \
    if (_a_ != _b_) printf("Assertion failed: expected %d, got %d \t\t\t\t | %s:%d (%s)\n", _a_, _b_, __FILE__, __LINE__, __FUNCTION__); } \
  while (0); 

#define assert_eq_ptr(got, expected)                                    \
  do {                                                                  \
    void *_b_ = (got); void *_a_ = (expected);                          \
    if (_a_ != _b_) printf("Assertion failed: expected %p, got %p \t\t\t\t | %s:%d (%s)\n", _a_, _b_, __FILE__, __LINE__, __FUNCTION__); } \
  while (0); 

void test_basic()
{
  treemap_t *t = treemap_create();
  assert_eq(treemap_size(t), 0);
  treemap_destroy(t);
}

void test_size()
{
  treemap_t *t = treemap_create();
  treemap_insert(t, 5, "String value");
  assert_eq(treemap_size(t), 1);
  treemap_insert(t, 2, "String value");
  assert_eq(treemap_size(t), 2);
  treemap_insert(t, 8, "String value");
  assert_eq(treemap_size(t), 3);
  treemap_insert(t, 6, "String value");
  assert_eq(treemap_size(t), 4);
  treemap_insert(t, 4, "String value");
  assert_eq(treemap_size(t), 5);
  treemap_insert(t, 1, "String value");
  assert_eq(treemap_size(t), 6);
  treemap_insert(t, 32, "String value");
  assert_eq(treemap_size(t), 7);
  treemap_destroy(t);
}

void test_keys_1()
{
  /*
   Attempt to creat the following tree
   by inserting keys 5, 2, 8, 6, 4, 1, 32
  
     treemap_t
        |
       _5_
      /   \
     2     8
    / \   / \
   1   4 6   32
  */

  treemap_t *t = treemap_create();
  treemap_insert(t, 5, "String value");
  treemap_insert(t, 2, "String value");
  treemap_insert(t, 8, "String value");
  treemap_insert(t, 6, "String value");
  treemap_insert(t, 4, "String value");
  treemap_insert(t, 1, "String value");
  treemap_insert(t, 32, "String value");

  int *keys = treemap_keys(t);
  assert_eq(keys[0], 5);
  assert_eq(keys[1], 2);
  assert_eq(keys[2], 1);
  assert_eq(keys[3], 4);
  assert_eq(keys[4], 8);
  assert_eq(keys[5], 6);
  assert_eq(keys[6], 32);
  free(keys);
  
  treemap_destroy(t);
}


void test_keys_2()
{
  /*
   Attempt to creat the following tree
   by inserting keys 5, 8, 2, 4, 6, 32, 1
  
     treemap_t
        |
       _5_
      /   \
     2     8
    / \   / \
   1   4 6   32
  */

  treemap_t *t = treemap_create();
  treemap_insert(t, 5, "String value");
  treemap_insert(t, 8, "String value");
  treemap_insert(t, 2, "String value");
  treemap_insert(t, 6, "String value");
  treemap_insert(t, 4, "String value");
  treemap_insert(t, 1, "String value");
  treemap_insert(t, 32, "String value");

  int *keys = treemap_keys(t);
  assert_eq(keys[0], 5);
  assert_eq(keys[1], 2);
  assert_eq(keys[2], 1);
  assert_eq(keys[3], 4);
  assert_eq(keys[4], 8);
  assert_eq(keys[5], 6);
  assert_eq(keys[6], 32);

  free(keys);
  treemap_destroy(t);
}

void test_has_keys()
{
  /*
   Attempt to creat the following tree
   by inserting keys 5, 8, 2, 4, 6, 32, 1
  
     treemap_t
        |
       _5_
      /   \
     2     8
    / \   / \
   1   4 6   32
  */

  treemap_t *t = treemap_create();

  assert_eq(treemap_has_key(t, 1), false);
  assert_eq(treemap_has_key(t, 2), false);
  assert_eq(treemap_has_key(t, 4), false);
  assert_eq(treemap_has_key(t, 5), false);
  assert_eq(treemap_has_key(t, 6), false);
  assert_eq(treemap_has_key(t, 8), false);
  assert_eq(treemap_has_key(t, 32), false);
  
  treemap_insert(t, 5, "String value");
  treemap_insert(t, 8, "String value");
  treemap_insert(t, 2, "String value");

  assert_eq(treemap_has_key(t, 1), false);
  assert_eq(treemap_has_key(t, 2), true);
  assert_eq(treemap_has_key(t, 4), false);
  assert_eq(treemap_has_key(t, 5), true);
  assert_eq(treemap_has_key(t, 6), false);
  assert_eq(treemap_has_key(t, 8), true);
  assert_eq(treemap_has_key(t, 32), false);
  
  treemap_insert(t, 6, "String value");
  treemap_insert(t, 4, "String value");
  treemap_insert(t, 1, "String value");
  treemap_insert(t, 32, "String value");

  assert_eq(treemap_has_key(t, 1), true);
  assert_eq(treemap_has_key(t, 2), true);
  assert_eq(treemap_has_key(t, 4), true);
  assert_eq(treemap_has_key(t, 5), true);
  assert_eq(treemap_has_key(t, 6), true);
  assert_eq(treemap_has_key(t, 8), true);
  assert_eq(treemap_has_key(t, 32), true);

  treemap_destroy(t);
}


void test_lookup()
{
  /*
   Attempt to creat the following tree
   by inserting keys 5, 8, 2, 4, 6, 32, 1
  
     treemap_t
        |
       _5_
      /   \
     2     8
    / \   / \
   1   4 6   32
  */

  treemap_t *t = treemap_create();

  assert_eq_ptr(treemap_lookup(t, 1), NULL);
  assert_eq_ptr(treemap_lookup(t, 2), NULL);
  assert_eq_ptr(treemap_lookup(t, 4), NULL);
  assert_eq_ptr(treemap_lookup(t, 5), NULL);
  assert_eq_ptr(treemap_lookup(t, 6), NULL);
  assert_eq_ptr(treemap_lookup(t, 8), NULL);
  assert_eq_ptr(treemap_lookup(t, 32), NULL);
  
  treemap_insert(t, 5, (void *)5);
  treemap_insert(t, 8, (void *)8);
  treemap_insert(t, 2, (void *)2);

  assert_eq_ptr(treemap_lookup(t, 1), NULL);
  assert_eq_ptr(treemap_lookup(t, 2), (void *)2);
  assert_eq_ptr(treemap_lookup(t, 4), NULL);
  assert_eq_ptr(treemap_lookup(t, 5), (void *)5);
  assert_eq_ptr(treemap_lookup(t, 6), NULL);
  assert_eq_ptr(treemap_lookup(t, 8), (void *)8);
  assert_eq_ptr(treemap_lookup(t, 32), NULL);
  
  treemap_insert(t, 6,  (void *)6);
  treemap_insert(t, 4,  (void *)4);
  treemap_insert(t, 1,  (void *)1);
  treemap_insert(t, 32, (void *)32);

  assert_eq_ptr(treemap_lookup(t, 1),  (void *)1);
  assert_eq_ptr(treemap_lookup(t, 2),  (void *)2);
  assert_eq_ptr(treemap_lookup(t, 4),  (void *)4);
  assert_eq_ptr(treemap_lookup(t, 5),  (void *)5);
  assert_eq_ptr(treemap_lookup(t, 6),  (void *)6);
  assert_eq_ptr(treemap_lookup(t, 8),  (void *)8);
  assert_eq_ptr(treemap_lookup(t, 32), (void *)32);

  treemap_destroy(t);
}

int main(void)
{
  test_basic();
  test_size();
  test_keys_1();
  test_keys_2();
  test_has_keys();
  test_lookup();

  puts("\nIf no errors are printed above -- all tests pass!");
}
