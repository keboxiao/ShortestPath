package com.path;

import java.util.ArrayList;
import java.util.List;


public class Dijkstra_Heap {
	// �������
	private int vs;
	// �ڵ�����
	private int nodeCount;
	// ���·�����飬0����·�����ȣ�1���ǽڵ����
	private int[][] dist;
	// ref���飬��¼ÿ���ڵ��ڶ��е�λ��
	private int[] ref;
	// �ýڵ��Ƿ��Ѿ��ҵ����·��
	private boolean[] find;
	// ǰ���ڵ�����
	private int[] prenode;
	// ·���б�
	private List<List<Integer>> pathList;
	// ͼ����
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
	 * Heap + Dijkstra�㷨ʵ��
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
			// ����vnear����vs���������нڵ��ǰ���ڵ㼰����
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
	 * ���ֵ�³�
	 * 
	 * @param index
	 * @param end
	 */
	private void maxDown(int index, int end) {
		int temp = dist[0][index];
		int left = left(index);

		while (left < end) {

			// �ж������ӽڵ��С
			if (left + 1 < end && dist[0][left + 1] < dist[0][left]) {
				left++;
			}
			// ��������ӽڵ㶼��temp��Ļ������³�����
			if (dist[0][left] > temp) {
				break;
			}
			// �����ӽڵ�͸��ڵ�
			changeKey(index, left);

			index = left;
			left = left(index);

		}
	}

	/**
	 * �ڵ����Ҷ��
	 * 
	 * @param i
	 */
	private int left(int i) {
		return ((i + 1) << 1) - 1;

	}

	/**
	 * Сֵ����
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
	 * �ڵ�ĸ��ڵ�
	 * 
	 * @param i
	 */
	private int parent(int i) {
		return (i - 1) / 2;
	}

	/**
	 * ��������ֵ
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
