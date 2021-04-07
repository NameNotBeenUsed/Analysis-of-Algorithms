import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;

public class AlgoTowers {

	public static void main(String[] args) {
		// args: integer 1~6, represent Task 1~6
		int task = 0;
		if(args.length > 0) {
			task = Integer.parseInt(args[0]);
		}
		else {
			System.err.println("Please input number 1~6 (represents task 1~6).");
			System.exit(0);
		}
		//read from the command line
		//line 1: m n h
		int m = 0, n = 0, h = 0;
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String parameters = "";
		try {
			parameters = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] split = parameters.trim().split("\\s+");
		m = Integer.parseInt(split[0]);
		n = Integer.parseInt(split[1]);
		h = Integer.parseInt(split[2]);
		
		int[][] matrix = new int[m][n];
		String[] tempRow = new String[n];
		for(int i = 0; i < m; i++) {
			try {
				tempRow = reader.readLine().trim().split("\\s+");
			}catch(IOException e) {
				e.printStackTrace();
			}
			for(int j = 0; j < n; j++) {
				matrix[i][j] = Integer.parseInt(tempRow[j]);
			}
		}
		
		//next m lines: n integers each line, (line i+1) p[i,1] p[i,2] ... p[i,n]
		//(i,j) i=1~m, j=1~n
		
		int[] position = new int[4];
		switch(task) {
			case 1:{
				//Task1
				position = squareSubmatrix(h, matrix);
				System.out.println(position[0] + " " + position[1] + " " + position[2] + " " + position[3]);
				break;
			}	
			case 2:{
				//Task2
				position = maximalSquare2(h, matrix);
				System.out.println(position[0] + " " + position[1] + " " + position[2] + " " + position[3]);
				break;
			}
			case 3:{
				//Task3
				position = maximalRectangle3(h, matrix);
				System.out.println(position[0] + " " + position[1] + " " + position[2] + " " + position[3]);
				break;
			}
			case 4:{
				//Task4
				position = maximalRectangle4(h, matrix);
				System.out.println(position[0] + " " + position[1] + " " + position[2] + " " + position[3]);
				break;
			}
			case 5:{
				//Task5
				position = maximalRectangle5(h, matrix);
				System.out.println(position[0] + " " + position[1] + " " + position[2] + " " + position[3]);
				break;
			}
			case 6:{
				//Task6
				position = maximalRectangle6(h, matrix);
				System.out.println(position[0] + " " + position[1] + " " + position[2] + " " + position[3]);
				break;
			}
		}	
	}

	/**ALG1: Design a Theta(MN) time Dynamic Programming algorithm for computing a largest area 
	 * square block with all cells have the height permit value at least h.
	 * Task1 Give a recursive implementation of ALG1 using memoization and O(mn) extra space.**/

	private static int[] squareSubmatrix(int h, int[][] matrix) {
		int rows = matrix.length;
		int cols = rows > 0? matrix[0].length : 0;
		int[][] cache = new int[matrix.length][matrix[0].length];
		int max = 0;
		int size = 0, x1 = 0, y1 = 0;
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				if(matrix[i][j] >= h) {
					if(max < squareSubmatrix(h, matrix, i, j, cache)) {
						max = squareSubmatrix(h, matrix, i, j, cache);
						x1 = i;
						y1 = j;
						size = cache[i][j];
					}
				}
					
			}
		}
		//x1 y1 x2 y2
		int[] position = {(x1 + 1), (y1 + 1), (x1 + size), (y1 + size)};
		return position;
	}
	
	private static int squareSubmatrix(int h, int[][] matrix, int i, int j, int[][] cache) {
		
		if(i == matrix.length || j == matrix[0].length) {
			return 0;
		}
		if(matrix[i][j] < h) {
			return 0;
		}
		
		if(cache[i][j] > 0) {
			return cache[i][j];
		}
		cache[i][j] = 1 + Math.min(Math.min(squareSubmatrix(h, matrix, i+1, j, cache), squareSubmatrix(h, matrix, i, j+1, cache)), squareSubmatrix(h, matrix, i+1, j+1, cache));
		return cache[i][j];
	}
	
	/**Task2 Give an iterative BottomUp implementation of ALG1 using O(n) extra space.**/

	private static int[] maximalSquare2(int h, int[][] matrix) {
		//rows: m, cols: n
		int m = matrix.length;
		int n = m > 0? matrix[0].length : 0;
		int[] position = new int[4];
		int[] dp = new int[n + 1];
		int maxsqlen = 0, prev = 0;
		int x2 = 0, y2 = 0;
		for(int i = 1; i <= m; i++) {
			for(int j = 1; j <= n; j++) {
				int temp = dp[j];
				if(matrix[i - 1][j - 1] >= h) {
					dp[j] = Math.min(Math.min(dp[j - 1], prev), dp[j]) + 1;
					if(dp[j] > maxsqlen) {
						maxsqlen = dp[j];
						x2 = i;
						y2 = j;
					}
				}
				else {
					dp[j] = 0;
				}
				prev = temp;
			}
		}
		//x1 y1 x2 y2
		position[0] = (x2 - maxsqlen + 1);
		position[1] = (y2 - maxsqlen + 1);
		position[2] = x2;
		position[3] = y2;
		return position;
	}
	
	/**ALG2 Design a Theta(m^3*n^3) Brute Force algorithm for computing a largest area rectangle block with all cells have the height permit value at least h.
	 * Task3 Give an implementation of ALG2 using O(1) extra space.**/

	public static int[] maximalRectangle3(int h, int[][] matrix) {
		int y = matrix.length;
		int x = y > 0? matrix[0].length : 0;
		int max = 0;
		int[] position = new int[4];
		for(int yFrom = 0; yFrom < y; yFrom++) {
			for(int yTo = yFrom; yTo < y; yTo++) {
				for(int xFrom = 0; xFrom < x; xFrom++) {
					for(int xTo = xFrom; xTo < x; xTo++) {
						if(isValid(h, matrix, xFrom, xTo, yFrom, yTo)) {
							//max = Math.max(max, getArea(xFrom, xTo, yFrom, yTo));
							if(max < getArea(xFrom, xTo, yFrom, yTo)) {
								max = getArea(xFrom, xTo, yFrom, yTo);
								//x1 y1 x2 y2
								position[0] = yFrom + 1;
								position[1] = xFrom + 1;
								position[2] = yTo + 1;
								position[3] = xTo + 1;
							}
						}
					}
				}
			}
		}
		return position;
	}
	
	private static int getArea(int xFrom, int xTo, int yFrom, int yTo) {
		return (xTo - xFrom + 1) * (yTo - yFrom + 1);
	}
	
	private static boolean isValid(int h, int[][] matrix, int xFrom, int xTo, int yFrom, int yTo) {
		for(int i = yFrom; i <= yTo; i++) {
			for(int j = xFrom; j <= xTo; j++) {
				if(matrix[i][j] < h) return false;
			}
		}
		return true;
	}
	
	/**ALG3 Design a Theta(mn) time Dynamic Programming algorithm for computing a largest area rectangle block with all cells have the height permit value at least h.
	 * Task4 Give an iterative BottomUp implementation of ALG3 using O(mn) extra space.**/

	private static int[] findMaxArea(int[] heights) {
		int len = heights.length;

        int area = 0;
        int y = 0, h = 0, w = 0;
        int[] info = new int[4];
        int[] newHeights = new int[len + 2];
        for (int i = 0; i < len; i++) {
            newHeights[i + 1] = heights[i];
        }
        len += 2;
        heights = newHeights;

        Deque<Integer> stack = new ArrayDeque<>();
        stack.addLast(0);

        for (int i = 1; i < len; i++) {
            while (heights[stack.peekLast()] > heights[i]) {
                int height = heights[stack.removeLast()];
                
                while(heights[stack.peekLast()] == height) {
                	stack.removeLast();
                }
                
                int width  = i - stack.peekLast() - 1;
                //area = Math.max(area, width * height);
                if(area < width * height) {
                	area = width * height;
                	y = i;
                	w = width;
                	h = height;
                }
            }
            stack.addLast(i);
        }
        //y height width max_area
        info[0] = y;
        info[1] = h;
        info[2] = w;
        info[3] = area;
        return info;
    }

	private static int[] maximalRectangle4(int h, int[][] matrix) {
        int n = matrix.length;
        int m = n == 0 ? 0 : matrix[0].length;
        int[][] memo = new int[n][m];

        // initialize
        // first row
        for (int i = 0; i < m; i++) {
            memo[0][i] = matrix[0][i] >= h ? 1 : 0;
        }

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (matrix[i][j] >= h) {
                    memo[i][j] = memo[i - 1][j] + 1;
                } else {
                    memo[i][j] = 0;
                }
            }
        }

        // find max
        int x2 = 0, y2 = 0, height = 0, width = 0, max = 0;
        int[] info = new int[4];
        for (int i = 0; i < n; i++) {
            //max = Math.max(max, findMaxArea(m + 1, memo[i]));
        	//info = findMaxArea(m + 1, memo[i]);
        	info = findMaxArea(memo[i]);
        	//y height width max_area
        	if(info[3] > max) {
        		x2 = i;
        		y2 = info[0];
        		height = info[1];
        		width = info[2];
        		max = info[3];
        	}
        }
        int x1 = (x2 - height + 1) + 1;
        int y1 = (y2 - width + 1) + 1;
        x2++;
        y2++;
        int[] position = {x1, y1 - 2, x2, y2 - 2};
        return position;
    }
	
	/**Task5 Give an iterative BottomUp implementation of ALG3 using O(n) extra space.**/
	
	private static int[] maximalRectangle5(int h, int[][] matrix) {
		int m = matrix.length;
		int n = m > 0? matrix[0].length : 0;
		int[] dp = new int[n];
		
		int x2 = 0, y2 = 0, height = 0, width = 0, max = 0;
		int[] info = new int[4];
		for(int i = 0; i < m; i++) {
			for(int j = 0; j < n; j++) {
				//update the state of this row's histogram using the last row's histogram by keeping track of the number of consecutive ones
				dp[j] = matrix[i][j] >= h? dp[j] + 1 : 0;
			}
			//update maxarea with the maximum area from this row's histogram
			//y height width max_area
			//info = findMaxArea(n + 1, dp);
			info = findMaxArea(dp);
			if(info[3] > max) {
				x2 = i;
        		y2 = info[0];
        		height = info[1];
        		width = info[2];
        		max = info[3];
			}
		}
		int x1 = (x2 - height + 1) + 1;
        int y1 = (y2 - width + 1) + 1;
        x2++;
        y2++;
        int[] position = {x1, y1 - 2, x2, y2 - 2};
        return position;
	}
	
	//Task6
    private static int[] maximalRectangle6(int h, int[][] matrix) {
        int m = matrix.length;
        int n = m > 0? matrix[0].length : 0;

        int[] left = new int[n]; // initialize left as the leftmost boundary possible
        int[] right = new int[n];
        int[] height = new int[n];

        Arrays.fill(right, n); // initialize right as the rightmost boundary possible

        int x2 = 0, y2 = 0, heigh = 0, y1 = 0, maxarea = 0;
        for(int i = 0; i < m; i++) {
            int cur_left = 0, cur_right = n;
            // update height
            for(int j = 0; j < n; j++) {
                if(matrix[i][j] >= h) height[j]++;
                else height[j] = 0;
            }
            // update left
            for(int j=0; j<n; j++) {
                if(matrix[i][j] >= h) left[j]=Math.max(left[j],cur_left);
                else {left[j]=0; cur_left=j+1;}
            }
            // update right
            for(int j = n - 1; j >= 0; j--) {
                if(matrix[i][j] >= h) right[j] = Math.min(right[j], cur_right);
                else {right[j] = n; cur_right = j;}    
            }
            // update area
            for(int j = 0; j < n; j++) {
            	if(maxarea < (right[j] - left[j]) * height[j]) {
            		maxarea = (right[j] - left[j]) * height[j];
            		x2 = i;
            		y2 = right[j];
            		heigh = height[j];
            		y1 = left[j];
            	}
            }
        }
        int[] position = {((x2 - heigh + 1) + 1), (y1 + 1), (x2 + 1), y2};
        return position;
    }
}
