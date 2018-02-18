package com.path;

import java.util.ArrayList;
import java.util.List;


public class Dijkstra_Heap {
	// 保存起点
	private int vs;
	// 节点总数
	private int nodeCount;
	// 最短路径数组，0行是路径长度，1行是节点序号
	private int[][] dist;
	// ref数组，记录每个节点在堆中的位置
	private int[] ref;
	// 该节点是否已经找到最短路径
	private boolean[] find;
	// 前驱节点数组
	private int[] prenode;
	// 路径列表
	private List<List<Integer>> pathList;
	// 图矩阵
	private int[][] graph;

	public Dijkstra_Heap(int[][] graph) {
		this.graph = graph;
		nodeCount = graph.length;
		dist = new int[2][nodeCount];
		ref = new int[nodeCount];
		find = new boolean[nodeCount];
		prenode = new int[nodeCount];
		pathList = new ArrayList<List<Integer>>();
		int i;
		for (i = 0; i < nodeCount; i++) {
			pathList.add(new ArrayList<Integer>());
		}
	}

	/**
	 * Heap + Dijkstra算法实现
	 */
	public void dijkstra(int vs) {

		this.vs = vs;
		for (int i = 0; i < nodeCount; i++) {
			if (graph[vs][i] != Integer.MAX_VALUE) {
				prenode[i] = vs;
			} else {
				prenode[i] = -1;
			}
			dist[0][i] = graph[vs][i];
			dist[1][i] = i;
			ref[i] = i;
			find[i] = false;
			minUp(i);
		}

		find[vs] = true;
		int qsize = nodeCount;
		while (qsize >= 1) {
			int vnear = dist[1][0];
			int min = dist[0][0];
			find[vnear] = true;
			changeKey(0, qsize - 1);
			maxDown(0, --qsize);
			// 根据vnear修正vs到其他所有节点的前驱节点及距离
			for (int k = 0; k < graph[vnear].length; k++) {
				if (!find[k] && graph[vnear][k] != Integer.MAX_VALUE && (min + graph[vnear][k]) < dist[0][ref[k]]) {
					dist[0][ref[k]] = min + graph[vnear][k];
					prenode[k] = vnear;
					minUp(ref[k]);
				}
			}
		}
	}

	public void showPath(int i) {
		System.out.print("v" + vs + "->v" + i + ", distance=" + dist[0][ref[i]] + " path:");
		for (int j = 0; j < pathList.get(i).size(); j++) {
			System.out.print("v" + pathList.get(i).get(j));
			if (j != pathList.get(i).size() - 1)
				System.out.print("->");
		}
		System.out.println();
	}

	public void buildPath(int r, int t) {
		if (r != vs)
			buildPath(prenode[r], t);
		pathList.get(t).add(r);
	}

	/**
	 * 最大值下沉
	 * 
	 * @param index
	 * @param end
	 */
	private void maxDown(int index, int end) {
		int temp = dist[0][index];
		int left = left(index);

		while (left < end) {

			// 判断左右子节点大小
			if (left + 1 < end && dist[0][left + 1] < dist[0][left]) {
				left++;
			}
			// 如果左右子节点都比temp大的话结束下沉操作
			if (dist[0][left] > temp) {
				break;
			}
			// 交换子节点和父节点
			changeKey(index, left);

			index = left;
			left = left(index);

		}
	}

	/**
	 * 节点的左叶子
	 * 
	 * @param i
	 */
	private int left(int i) {
		return ((i + 1) << 1) - 1;

	}

	/**
	 * 小值上升
	 * 
	 * @param n
	 */
	private void minUp(int n) {
		int f = parent(n);
		while (n >= 1 && dist[0][f] > dist[0][n]) {
			changeKey(f, n);
			n = f;
			f = parent(n);
		}
	}

	/**
	 * 节点的父节点
	 * 
	 * @param i
	 */
	private int parent(int i) {
		return (i - 1) / 2;
	}

	/**
	 * 交换两个值
	 * 
	 * @param a
	 * @param b
	 */
	private void changeKey(int a, int b) {
		int n = dist[1][a];
		int m = dist[1][b];
		int temp = ref[n];
		ref[n] = ref[m];
		ref[m] = temp;
		temp = dist[0][a];
		dist[0][a] = dist[0][b];
		dist[0][b] = temp;
		temp = dist[1][a];
		dist[1][a] = dist[1][b];
		dist[1][b] = temp;
	}
}
