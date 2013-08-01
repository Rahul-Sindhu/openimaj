/**
 * Copyright (c) 2011, The University of Southampton and the individual contributors.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *   * 	Redistributions of source code must retain the above copyright notice,
 * 	this list of conditions and the following disclaimer.
 *
 *   *	Redistributions in binary form must reproduce the above copyright notice,
 * 	this list of conditions and the following disclaimer in the documentation
 * 	and/or other materials provided with the distribution.
 *
 *   *	Neither the name of the University of Southampton nor the names of its
 * 	contributors may be used to endorse or promote products derived from this
 * 	software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.openimaj.ml.clustering;

import gnu.trove.list.array.TIntArrayList;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

import org.openimaj.io.IOUtils;


/**
 * Interface to describe objects that are the result of the clustering where
 * the training data is implicitly clustered
 *
 * @author Jonathon Hare (jsh2@ecs.soton.ac.uk)
 */
public class IndexClusters implements Clusters {
	
	private int[][] clusters;
	private int nEntries;
	
	/**
	 * used only to initailise for {@link IOUtils}
	 */
	public IndexClusters() {
	}
	
	/**
	 * @param clusters the clusters
	 * @param nEntries the number of entries
	 */
	public IndexClusters(int[][] clusters, int nEntries) {
		this.nEntries = nEntries;
		this.clusters = clusters;
	}
	
	/**
	 * @param clusters the clusters
	 */
	public IndexClusters(int[][] clusters) {
		this.nEntries = 0;
		this.clusters = clusters;
		for (int i = 0; i < clusters.length; i++) {
			this.nEntries += clusters[i].length;
		}
	}
	
	/**
	 * @param assignments convert a list of cluster assignments to a 2D array to cluster to assignments
	 */
	public IndexClusters(int[] assignments) {
		this.nEntries = assignments.length;
		Map<Integer,TIntArrayList> clusters = new HashMap<Integer, TIntArrayList>();
		for (int i = 0; i < assignments.length; i++) {
			int ass = assignments[i];
			TIntArrayList current = clusters.get(ass);
			if(current == null){
				clusters.put(ass, current = new TIntArrayList());
			}
			current.add(i);
		}
		int clustersSeen = 0;
		this.clusters = new int[clusters.size()][];
		for (Entry<Integer, TIntArrayList> i : clusters.entrySet()) {
			this.clusters[clustersSeen] = i.getValue().toArray(); 
			clustersSeen++;
		}
	}

	/**
	 * Get the number of clusters.
	 *
	 * @return number of clusters.
	 */
	public int[][] clusters(){
		return clusters;
	}
	
	/**
	 * Get the number of data entries
	 *
	 * @return the number of data entries.
	 */
	public int numEntries() {
		return nEntries;
	}

	/**
	 * Get the number of clusters.
	 *
	 * @return number of clusters.
	 */
	public int numClusters(){
		return this.clusters.length;
	}

	@Override
	public void readASCII(Scanner in) throws IOException {
		this.clusters = new int[in.nextInt()][];
		this.nEntries = in.nextInt();
		for (int i = 0; i < this.nEntries;) {
			int cluster = in.nextInt();
			int count = in.nextInt();
			i+=count;
			this.clusters[cluster] = new int[count]; 
			for (int j = 0; j < count; j++) {
				this.clusters[cluster][j] = in.nextInt();
			}
		}
	}

	@Override
	public String asciiHeader() {
		return "IDX"+CLUSTER_HEADER;
	}

	@Override
	public void readBinary(DataInput in) throws IOException {
		this.clusters = new int[in.readInt()][];
		this.nEntries = in.readInt();
		for (int i = 0; i < this.nEntries;) {
			int cluster = in.readInt();
			int count = in.readInt();
			i+=count;
			this.clusters[cluster] = new int[count]; 
			for (int j = 0; j < count; j++) {
				this.clusters[cluster][j] = in.readInt();
			}
		}
	}

	@Override
	public byte[] binaryHeader() {
		return asciiHeader().getBytes();
	}

	@Override
	public void writeASCII(PrintWriter out) throws IOException {
		out.println(this.numClusters());
		out.println(this.nEntries);
		for (int i = 0; i < this.clusters.length; i++) {
			int[] cluster = this.clusters[i];
			out.println(i);
			out.println(cluster.length);
			for (int j = 0; j < cluster.length; j++) {
				out.println(cluster[j]);
			}
		}
	}

	@Override
	public void writeBinary(DataOutput out) throws IOException {
		out.writeInt(this.numClusters());
		out.writeInt(nEntries);
		for (int i = 0; i < this.clusters.length; i++) {
			int[] cluster = this.clusters[i];
			out.writeInt(i);
			out.writeInt(cluster.length);
			for (int j = 0; j < cluster.length; j++) {
				out.writeInt(cluster[j]);
			}
		}
	}
	@Override
	public String toString() {
		int[][] clusters = this.clusters();
		int i = 0;
		StringWriter sw = new StringWriter();
		PrintWriter out = new PrintWriter(sw);
		out.println("N-Clusters: " + this.numClusters());
		out.println("Entities: " + this.numEntries());
		String str = sw.toString();
		for (int[] member : clusters) {
			str += String.format("%d %s\n",i++,Arrays.toString(member));
		}
		return str;
	}
}