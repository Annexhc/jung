package edu.uci.ics.jung.layout3d;

import com.google.common.collect.Sets;
import com.google.common.graph.Graph;
import edu.uci.ics.jung.graph.CTreeNetwork;
import edu.uci.ics.jung.graph.MutableCTreeNetwork;
import edu.uci.ics.jung.graph.TreeNetworkBuilder;
import edu.uci.ics.jung.graph.util.TestGraphs;
import edu.uci.ics.jung.layout.algorithms.LayoutAlgorithm;
import edu.uci.ics.jung.layout.model.LoadingCacheLayoutModel;
import edu.uci.ics.jung.layout.model.PointModel;
import edu.uci.ics.jung.layout3d.algorithms.BalloonLayoutAlgorithm;
import edu.uci.ics.jung.layout3d.algorithms.KKLayoutAlgorithm;
import edu.uci.ics.jung.layout3d.algorithms.SphereLayoutAlgorithm;
import edu.uci.ics.jung.layout3d.algorithms.SpringLayoutAlgorithm;
import java.util.Collection;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author Tom Nelson */
public class LayoutAlgorithmTest {

  private static final Logger log = LoggerFactory.getLogger(LayoutAlgorithmTest.class);

  Graph<String> graph;
  LoadingCacheLayoutModel<String, TestPointModel.Point> layoutModel;
  PointModel<TestPointModel.Point> pointModel = new TestPointModel();

  @Test
  public void testLayoutAlgorithms() {
    graph = TestGraphs.getDemoGraph().asGraph();
    layoutModel =
        LoadingCacheLayoutModel.<String, TestPointModel.Point>builder()
            .setGraph(graph)
            .setPointModel(pointModel)
            .setSize(500, 500)
            .build();
    testLayoutAlgorithm(new SpringLayoutAlgorithm<>());
    testLayoutAlgorithm(new KKLayoutAlgorithm<>());
    // ISOM seems to put some nodes in the same location, so the test will fail
    //        testLayoutAlgorithm(new ISOMLayoutAlgorithm<String, TestPointModel.Point>(pointModel));
    testLayoutAlgorithm(new SphereLayoutAlgorithm<>());
  }

  @Test
  public void testBalloonLayoutAlgorithms() {
    graph = createTree().asGraph();
    layoutModel =
        LoadingCacheLayoutModel.<String, TestPointModel.Point>builder()
            .setGraph(graph)
            .setPointModel(pointModel)
            .setSize(500, 500)
            .build();
    testLayoutAlgorithm(new BalloonLayoutAlgorithm<>());
  }

  private void testLayoutAlgorithm(LayoutAlgorithm<String, TestPointModel.Point> layoutAlgorithm) {
    layoutModel.clear();
    layoutModel.accept(layoutAlgorithm);
    testUniqueLocations();
  }

  private void testUniqueLocations() {
    Set<TestPointModel.Point> locations = Sets.newHashSet();
    Collection<String> nodes = layoutModel.getGraph().nodes();
    for (String node : nodes) {
      TestPointModel.Point p = layoutModel.get(node);
      locations.add(layoutModel.get(node));
    }
    // make sure that the algorithm as provided unique locations for all nodes

    Assert.assertEquals(nodes.size(), locations.size());
  }

  private CTreeNetwork<String, Integer> createTree() {
    MutableCTreeNetwork<String, Integer> tree =
        TreeNetworkBuilder.builder().expectedNodeCount(27).build();

    tree.addNode("root");

    int edgeId = 0;
    tree.addEdge("root", "V0", edgeId++);
    tree.addEdge("V0", "V1", edgeId++);
    tree.addEdge("V0", "V2", edgeId++);
    tree.addEdge("V1", "V4", edgeId++);
    tree.addEdge("V2", "V3", edgeId++);
    tree.addEdge("V2", "V5", edgeId++);
    tree.addEdge("V4", "V6", edgeId++);
    tree.addEdge("V4", "V7", edgeId++);
    tree.addEdge("V3", "V8", edgeId++);
    tree.addEdge("V6", "V9", edgeId++);
    tree.addEdge("V4", "V10", edgeId++);

    tree.addEdge("root", "A0", edgeId++);
    tree.addEdge("A0", "A1", edgeId++);
    tree.addEdge("A0", "A2", edgeId++);
    tree.addEdge("A0", "A3", edgeId++);

    tree.addEdge("root", "B0", edgeId++);
    tree.addEdge("B0", "B1", edgeId++);
    tree.addEdge("B0", "B2", edgeId++);
    tree.addEdge("B1", "B4", edgeId++);
    tree.addEdge("B2", "B3", edgeId++);
    tree.addEdge("B2", "B5", edgeId++);
    tree.addEdge("B4", "B6", edgeId++);
    tree.addEdge("B4", "B7", edgeId++);
    tree.addEdge("B3", "B8", edgeId++);
    tree.addEdge("B6", "B9", edgeId++);

    return tree;
  }
}
