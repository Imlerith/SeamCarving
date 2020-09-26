# Seam Carving
The seam carving algorithm allows to resize images without distortion so that they can be displayed without losses in content on cell phones and web browsers. Seam carving removes, vertically or horizontally, streaks of pixels or *seams* which have the smallest *energy*. The energy of a pixel can be computed via e.g. the *dual-gradient energy function*.

The search of the seams with the smallest energy is then done via shortest-path search in an edge-weighted pixel DAG. In the current script, [https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm](Dijkstra's algorithm) is applied.
