package net.g2lab.io;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.g2lab.feature.Feature;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.SchemaException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jump.io.DriverProperties;
import com.vividsolutions.jump.io.ShapefileWriter;


public class ShapeIO {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());

	public ShapeIO() {
	}
	
	public FeatureCollection<SimpleFeatureType, SimpleFeature> readSimpleFeatures(URL url) throws IOException {
		LOG.info("reading features from shapefile " + url);
		FeatureIterator<SimpleFeature> iterator = null;
		
		try {
			Map<String, Serializable> connectParameters = new HashMap<String, Serializable>();

			connectParameters.put("url", url);
			connectParameters.put("create spatial index", true);
			DataStore dataStore = DataStoreFinder.getDataStore(connectParameters);

			String[] typeNames = dataStore.getTypeNames();
			String typeName = typeNames[0];

			FeatureSource<SimpleFeatureType, SimpleFeature> featureSource;
//			FeatureCollection<SimpleFeatureType, SimpleFeature> collection;

			featureSource = dataStore.getFeatureSource(typeName);
			return featureSource.getFeatures();

		} catch (NullPointerException ex) {
			throw new IOException(ex);
		} finally {
			if (iterator != null) {
				iterator.close();
			}
		}

	}
	public void writeFeatures(Collection<Feature> features, URL url) throws IOException {

		if (features.size() == 0) {
			LOG.info("no features to write!");
			return;
		}
		
		SimpleFeatureType featureType;
		try {
			String featureTypeString = features.iterator().next().getFeatureTypeString();
			featureType = DataUtilities.createType("feature", featureTypeString);
		} catch (SchemaException e) {
			LOG.error("could not create feature type: " + e, e);
			return;
		}
			
		DataStoreFactorySpi dataStoreFactory = new ShapefileDataStoreFactory();

		Map<String, Serializable> params = new HashMap<String, Serializable>();
		params.put("url", url);
//		params.put("create spatial index", Boolean.TRUE);

		ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);
		newDataStore.createSchema(featureType);
//		newDataStore.forceSchemaCRS(DefaultGeographicCRS.WGS84);

		Transaction transaction = new DefaultTransaction("create");

		String typeName = newDataStore.getTypeNames()[0];
		FeatureStore<SimpleFeatureType, SimpleFeature> featureStore = (FeatureStore<SimpleFeatureType, SimpleFeature>) newDataStore.getFeatureSource(typeName);
		featureStore.setTransaction(transaction);
		
		List<SimpleFeature> simpleFeatures = new ArrayList<SimpleFeature>();
		simpleFeatures.addAll(Feature.getAsSimpleFeatures(features));
		try {
			DefaultFeatureCollection featureCollection = new DefaultFeatureCollection();
			featureCollection.addAll(simpleFeatures);
			featureStore.addFeatures(featureCollection);
			transaction.commit();
			LOG.info("written " + featureCollection.size() + " features to " + url);
		} catch (Exception e) {
			LOG.error(e, e);
			transaction.rollback();
		} finally {
			transaction.close();
		}

	}

	private Collection<? extends SimpleFeature> getSubset(
			Collection<SimpleFeature> simpleFeatures, int start, int end) {
		return null;
	}
	

}