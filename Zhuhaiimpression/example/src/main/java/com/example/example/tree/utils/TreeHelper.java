package com.example.example.tree.utils;

import android.util.Log;

import com.example.example.R;
import com.example.example.tree.utils.annotation.TreeNodeId;
import com.example.example.tree.utils.annotation.TreeNodeName;
import com.example.example.tree.utils.annotation.TreeNodePid;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TreeHelper {

	private static <T> List<Node> convertDatas2Nodes(List<T> datas) throws IllegalAccessException {
		List<Node> nodes = new ArrayList<Node>();
		for (T t : datas) {
			Class clazz = t.getClass();
			Field fields[] = clazz.getDeclaredFields();
			Log.d("vae_tag", fields.length + "   fields length");
			int id = -1, pid = -1;
			String name = null;
			for (Field field : fields) {
				if (null != field.getAnnotation(TreeNodeId.class)) {
					field.setAccessible(true);
					id = field.getInt(t);
				}
				if (null != field.getAnnotation(TreeNodePid.class)) {
					field.setAccessible(true);
					pid = field.getInt(t);
				}
				if (null != field.getAnnotation(TreeNodeName.class)) {
					field.setAccessible(true);
					name = (String) field.get(t);
				}
			}
			Node node = new Node(id, pid, name);
			nodes.add(node);
		}

		/**
		 * 设置关系
		 */
		for (int i = 0; i < nodes.size(); i++) {
			Node n = nodes.get(i);
			for (int j = i + 1; j < nodes.size(); j++) {
				Node m = nodes.get(j);
				if (m.getPid() == n.getId()) {
					n.getChildren().add(m);
					m.setParent(n);
				} else if (m.getId() == n.getPid()) {
					m.getChildren().add(n);
					n.setParent(m);

				}
			}
		}

		/**
		 * 设置icon
		 */
		for (Node node : nodes) {
			setNodeIcon(node);
		}

		return nodes;
	}

	private static void setNodeIcon(Node node) {
		if (node.getChildren().size() > 0 && node.isExpand()) {
			node.setIcon(R.mipmap.ic_launcher);
		} else if (node.getChildren().size() > 0 && !node.isExpand()) {
			node.setIcon(R.mipmap.ic_launcher);
		} else {
			node.setIcon(-1);
		}
	}

	public static <T> List<Node> getSortNodes(List<T> datas, int defualExpandLevel) throws IllegalAccessException {
		List<Node> sortNodes = new ArrayList<>();
		List<Node> nodes = convertDatas2Nodes(datas);
		/**
		 * 获取树的跟节点
		 */
		List<Node> rootNodes = getRootNodes(nodes);
		for(Node node : rootNodes) {
			addNode(sortNodes, node, defualExpandLevel, 1);
		}
		return sortNodes;
	}

	public static List<Node> filterVisibleNodes(List<Node> nodes) {
		List<Node> result = new ArrayList<>();
		for (Node node : nodes) {
			if (node.isRoot() || node.isParentExpand()) {
				setNodeIcon(node);
				result.add(node);
			}
		}
		return result;
	}

	private static List<Node> getRootNodes(List<Node> nodes) {
		List<Node> rootNodes = new ArrayList<>();
		for (Node node : nodes) {
			if (node.isRoot()) {
				rootNodes.add(node);
			}
		}
		return rootNodes;
	}

	private static void addNode(List<Node> result, Node node, int defualExpandLevel, int currentLevel) {
		result.add(node);

		if (defualExpandLevel > currentLevel) {
			node.setExpand(true);
		}
		if (node.isLeaf()) {
			return ;
		}
		for (int i = 0; i < node.getChildren().size(); i++) {
			addNode(result, node.getChildren().get(i), defualExpandLevel, currentLevel + 1);
		}
	}
}
