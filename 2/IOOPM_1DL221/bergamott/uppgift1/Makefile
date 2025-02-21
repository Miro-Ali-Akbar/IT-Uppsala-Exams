compile:
	gcc -Wall -g yourcode.c driver.c

test: compile
	./a.out

memtest: compile
	valgrind --leak-check=full ./a.out

clean:
	rm -f *.o a.out
