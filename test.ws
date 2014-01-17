int test(int test1, int test2) {
	if (test1 < 100 && test2 > 0)
		test(test1+1, test2 - 1);
}