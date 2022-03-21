package speakap.rijksmuseum.commons.utils;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * https://gist.github.com/rogerhu/17aca6ad4dbdb3fa5892 .
 */
public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private static final int THRESHOLD_VISIBLE_ITEM_COUNT = 5;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = THRESHOLD_VISIBLE_ITEM_COUNT;

    // The current offset index of data you have loaded
    private int currentPage = 0;

    // The total number of items in the dataset after the last load
    private int previousTotalItemCount = 0;

    // True if we are still waiting for the last set of data to load.
    private boolean loading = true;

    // Sets the starting page index
    private int startingPageIndex = 0;

    private RecyclerView.LayoutManager layoutManager;

    /**
     * Constructor for EndlessRecyclerViewScrollListener.
     *
     * @param layoutManager Linear layout manager
     */
    public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    /**
     * Constructor for EndlessRecyclerViewScrollListener.
     *
     * @param layoutManager Grid layout manager
     */
    public EndlessRecyclerViewScrollListener(GridLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    /**
     * Change layout manager.
     *
     * @param layoutManager Linear layout manager
     */
    public void changeLayoutManager(GridLayoutManager layoutManager) {
        // TODO: 28.12.2017 change threshold with layout manager
        this.layoutManager = layoutManager;
    }

    /**
     * Change layout manager.
     *
     * @param linearLayoutManager Grid layout manager
     */
    public void changeLayoutManager(LinearLayoutManager linearLayoutManager) {
        this.layoutManager = linearLayoutManager;
    }

    /**
     * Change layout manager.
     *
     * @param staggeredGridLayoutManager Staggered layout manager
     */
    public void changeLayoutManager(StaggeredGridLayoutManager staggeredGridLayoutManager) {
        this.layoutManager = staggeredGridLayoutManager;
    }

    /**
     * Get last visible item.
     *
     * @param lastVisibleItemPositions last visible item positions
     * @return last visible item
     */
    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more data,
    // but first we check if we are waiting for the previous load to finish.
    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        int lastVisibleItemPosition = 0;
        int totalItemCount = layoutManager.getItemCount();

        if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions
                    = ((StaggeredGridLayoutManager) layoutManager)
                    .findLastVisibleItemPositions(null);

            // get maximum element within the list
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
        } else if (layoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        }

        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // threshold should reflect how many total columns there are too
        if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
            currentPage++;
            onLoadMore(currentPage, totalItemCount, view);
            loading = true;
        }
    }

    /**
     * Call whenever performing new searches.
     */
    public void resetState() {
        this.currentPage = this.startingPageIndex;
        this.previousTotalItemCount = 0;
        this.loading = true;
    }

    /**
     * Defines the process for actually loading more data based on page.
     *
     * @param page            page
     * @param totalItemsCount total items count
     * @param view            recycler view
     */
    public abstract void onLoadMore(int page, int totalItemsCount, RecyclerView view);

}
