#ifndef __yourcode__
#define __yourcode__

#include <stdlib.h>
#include <stdbool.h>
#include <stdio.h>
#include <string.h>

//typedef int(*void_cmp)(void *e1, void *e2);
typedef char *(*elem_to_string)(void *e);

typedef struct link link_t;
typedef struct priority_queue priority_queue_t;

struct link
{
  link_t *next;
  void *element;
  int priority;
};

/// Create a new (empty) priority queue -- compare is a function that defines the sort order for the elements.
priority_queue_t *priority_queue_create();

/// Insert element into priority queue according to the sort order
void priority_queue_insert_with_priority(priority_queue_t *priority_queue, void *element, int priority);

/// Merge the two priority queues into dest, leaving source empty (note: implemented so that links move, not just elements!)
void priority_queue_merge(priority_queue_t *source, priority_queue_t *dest);

/// Return the number of elements in the priority queue
int priority_queue_size(priority_queue_t *list);

/// Remove the priority queue and its elements from memory
void priority_queue_destroy(priority_queue_t *list);

/// Return the string representation for the priority queue, e.g. [(1,a), (1,b), (4,c)]

char *priority_queue_print(priority_queue_t *priority_queue, elem_to_string to_string);

/// Return the first link in the priority queue for testing

link_t *priority_queue_first(priority_queue_t *priority_queue);

//remove the element from the queue that has the highest priority, and return it
link_t *priority_queue_pull_highest_priority_element(priority_queue_t *priority_queue);

#endif

