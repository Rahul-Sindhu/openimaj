package org.openimaj.image.processing.face.similarity;

import org.openimaj.feature.FloatFVComparison;
import org.openimaj.image.FImage;
import org.openimaj.image.processing.face.alignment.AffineAligner;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.FaceDetector;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;
import org.openimaj.image.processing.face.feature.DoGSIFTFeature;
import org.openimaj.image.processing.face.feature.FacePatchFeature;
import org.openimaj.image.processing.face.feature.FacialFeatureFactory;
import org.openimaj.image.processing.face.feature.LocalLBPHistogram;
import org.openimaj.image.processing.face.feature.comparison.DoGSIFTFeatureComparator;
import org.openimaj.image.processing.face.feature.comparison.FaceFVComparator;
import org.openimaj.image.processing.face.feature.comparison.FacialFeatureComparator;
import org.openimaj.image.processing.face.feature.comparison.ReversedLtpDtFeatureComparator;
import org.openimaj.image.processing.face.feature.ltp.ReversedLtpDtFeature;
import org.openimaj.image.processing.face.feature.ltp.TruncatedWeighting;
import org.openimaj.image.processing.face.keypoints.FKEFaceDetector;
import org.openimaj.image.processing.face.keypoints.KEDetectedFace;

public enum PredefinedStrategy{
	SIFT{
		@Override
		public FaceSimilarityStrategy<?, ?, FImage> strategy() {
			FacialFeatureComparator<DoGSIFTFeature> comparator = new DoGSIFTFeatureComparator();
			FaceDetector<DetectedFace,FImage> detector = new HaarCascadeDetector(80);
			FacialFeatureFactory<DoGSIFTFeature,DetectedFace> factory = new DoGSIFTFeature.Factory();
			
			return FaceSimilarityStrategy.build(detector, factory, comparator);
		}

		@Override
		public String description() {
			return "SIFT features using a TransformedOneToOnePointModel for feature matching and the SIFT vector for comparison.";
		}
	},
	LOCAL_TRINARY_PATTERN{
		@Override
		public FaceSimilarityStrategy<?, ?, FImage> strategy() {
			FacialFeatureComparator<ReversedLtpDtFeature> comparator = new ReversedLtpDtFeatureComparator();
			FKEFaceDetector detector = new FKEFaceDetector();
			FacialFeatureFactory<ReversedLtpDtFeature, KEDetectedFace> factory = 
				new ReversedLtpDtFeature.Factory<KEDetectedFace>(
						new AffineAligner(), 
						new TruncatedWeighting()
				);
			
			return FaceSimilarityStrategy.build(detector, factory, comparator);
		}

		@Override
		public String description() {
			return "Local Ternary Pattern feature using truncated distance-maps for comparison. Faces aligned using affine transform.";
		}
	},
	FACEPATCH_EUCLIDEAN{
		@Override
		public FaceSimilarityStrategy<?, ?, FImage> strategy() {
			FacialFeatureFactory<FacePatchFeature, KEDetectedFace> factory = new FacePatchFeature.Factory();
			FacialFeatureComparator<FacePatchFeature> comparator = new FaceFVComparator<FacePatchFeature>(FloatFVComparison.EUCLIDEAN);
			FKEFaceDetector detector = new FKEFaceDetector();
			
			return FaceSimilarityStrategy.build(detector, factory, comparator);
		}
		
		@Override
		public String description() {
			return "Patched facial features, compared as a big vector using Euclidean distance.";
		}
	},
	LOCAL_BINARY_PATTERN{
		@Override
		public FaceSimilarityStrategy<?, ?, FImage> strategy() {
//			FacialFeatureFactory<LocalLBPHistogram, KEDetectedFace> factory = new LocalLBPHistogram.Factory<KEDetectedFace>(new AffineAligner(), 20, 20, 8, 1);
//			FacialFeatureFactory<LocalLBPHistogram, KEDetectedFace> factory = new 	LocalLBPHistogram.Factory<KEDetectedFace>(new AffineAligner(), 7, 7, 16, 4);
			FacialFeatureFactory<LocalLBPHistogram, KEDetectedFace> factory = new LocalLBPHistogram.Factory<KEDetectedFace>(new AffineAligner(), 7, 7, 8, 2);
			FacialFeatureComparator<LocalLBPHistogram> comparator = new FaceFVComparator<LocalLBPHistogram>(FloatFVComparison.CHI_SQUARE);
			FKEFaceDetector detector = new FKEFaceDetector();
			
			return FaceSimilarityStrategy.build(detector, factory, comparator);
		}

		@Override
		public String description() {
			return "Local LBP histograms compared using Chi squared distance. Faces aligned using affine transform.";
		}
		
	},
	;
	public abstract FaceSimilarityStrategy<?,?,FImage> strategy();
	public abstract String description();
	
	public static void main(String[] args) {
	}
}
