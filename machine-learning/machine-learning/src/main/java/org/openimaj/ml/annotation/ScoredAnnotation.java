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
package org.openimaj.ml.annotation;

/**
 * An annotation that was produced automatically with a given confidence.
 * 
 * @author Jonathon Hare (jsh2@ecs.soton.ac.uk)
 * 
 * @param <ANNOTATION>
 *            Type of annotation
 */
public class ScoredAnnotation<ANNOTATION> implements Comparable<ScoredAnnotation<ANNOTATION>> {
	/**
	 * The annotation
	 */
	public ANNOTATION annotation;

	/**
	 * The confidence of the annotation
	 */
	public float confidence;

	/**
	 * Construct with the given annotation and confidence
	 * 
	 * @param annotation
	 *            the annotation
	 * @param confidence
	 *            the confidence level
	 */
	public ScoredAnnotation(ANNOTATION annotation, float confidence) {
		this.annotation = annotation;
		this.confidence = confidence;
	}

	@Override
	public String toString() {
		return "(" + annotation.toString() + ", " + confidence + ")";
	}

	@Override
	public int compareTo(ScoredAnnotation<ANNOTATION> o) {
		if (confidence < o.confidence)
			return -1;
		if (confidence > o.confidence)
			return 1;
		return 0;
	}
}