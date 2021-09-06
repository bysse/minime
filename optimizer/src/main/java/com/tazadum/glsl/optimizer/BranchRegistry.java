package com.tazadum.glsl.optimizer;

import com.tazadum.glsl.language.ast.Node;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Objects;

public class BranchRegistry {
    private static final int PRE_ALLOC = 1000;
    private HashSet<Location> pointSet;

    public BranchRegistry() {
        this.pointSet = new HashSet<>(PRE_ALLOC);
    }

    public BranchRegistry(HashSet<Location> pointSet) {
        this.pointSet = pointSet;
    }

    /**
     * Remove all invalid entries
     */
    public void cleanUp() {
        pointSet.removeIf(Location::isInvalid);
    }

    /**
     * Attempts to claim a node for an optimizer.
     * If the point was already recorded the method will return false.
     *
     * @param node
     * @param optimizer Class of the optimizer, this will be used as the tag.
     * @return True is the point is available otherwise false will be returned.
     */
    public boolean claimPoint(Node node, Class<?> optimizer) {
        return claimPoint(node, optimizer.getName());
    }

    /**
     * Attempts to claim a node for an optimizer.
     * If the point was already recorded the method will return false.
     *
     * @param node
     * @param tag
     * @return True is the point is available otherwise false will be returned.
     */
    public boolean claimPoint(Node node, String tag) {
        pointSet.removeIf(location -> location.getNode() == null);
        return pointSet.add(new Location(node, tag));
    }

    /**
     * Clones the branch registry.
     */
    public BranchRegistry remap() {
        cleanUp();
        return new BranchRegistry(new HashSet<>(pointSet));
    }

    private static class Location {
        private WeakReference<Node> node;
        private String tag;
        private int id;

        public Location(Node node, String tag) {
            this.node = new WeakReference<>(node);
            this.tag = tag;
            this.id = node.getId();
        }

        public boolean isInvalid() {
            return node.get() != null;
        }

        public Node getNode() {
            return node.get();
        }

        public int getId() {
            return id;
        }

        public String getTag() {
            return tag;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Location location = (Location) o;

            if (id != location.id) return false;
            return Objects.equals(tag, location.tag);
        }

        @Override
        public int hashCode() {
            int result = tag != null ? tag.hashCode() : 0;
            result = 31 * result + id;
            return result;
        }
    }
}
