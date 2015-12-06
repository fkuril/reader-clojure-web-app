(ns reader-web-app.pages.landing
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [reader-web-app.actions.common :as actions]))

;; Cover
(defn book-card [book owner]
  (reify
    om/IRender
    (render [_]
      (println book (:id book))
      (dom/div #js {:className "book-card-container"}
        (dom/div #js {:className "book-card"}
          (dom/div #js {:className "book-card-desc-container"}
            (dom/div #js {:className "book-card-cover-container"}
              (dom/img #js {:className "book-card-cover"
                            :src (if (nil? (:cover book)) (.png (js/Trianglify #js {:width 200, :height 280})) (:cover book))
                            :alt "cover"})
              (dom/i #js {:className "fa fa-heart-o"}))
            (dom/div #js {:className "book-description"}
              "Name ASDASDasdas \n Author"))
          (dom/div #js {:className "book-card-progress-container"}
            (dom/div #js {:className "book-card-progress"})
            (dom/button #js {:className "read-btn"}
              (dom/span #js {:className "read-btn-text"} "Read"))))))))

(defn books-grid [books owner]
  (reify
    om/IInitState
    (init-state [_]
      {:welc-ol-vis true})
    om/IDidMount
    (did-mount [this]
      (actions/get-all-books))
    om/IRenderState
    (render-state [this state]
      (dom/div #js {:className "app-container"}
        (dom/div #js {:className "heading"}
          (dom/div #js {:className "logo"}
            (dom/i #js {:className "fa fa-book" } "")
            " "
            (dom/span nil "Shelf")))

        (dom/div #js {:className "book-cards-grid-container"}
          (dom/div #js {:className "book-cards-grid"}
            (om/build-all book-card books)))

        (dom/div #js {:className "book-preview-overlay"}
          (dom/div #js {:className "book-card-preview"}))

        (if (true? (:welc-ol-vis state))
          (dom/div #js {:className "welcome"}
            (dom/div #js {:className "overlay-dropzone"} "")
            (dom/div #js {:className "welcome-text-container"}
              (dom/p #js {:className "welcome-app-description"}
                "Welcome to "
                (dom/span #js {:className "logo"} "Shelf")
                " : the best place to just pick up a book, start reading and continue on any device even if you are offline.")
              (dom/p #js {:className "welcome-dropzone-description"} "Drop your book anywhere on this page to start reading.")
              (dom/i #js {:className "fa fa-plus-square-o drop-icon"} "")
              (dom/p #js {:className "welcome-show-library"}
                "Or "
                (dom/a #js {:className "sl-link-btn"
                            :onClick (fn [e] (om/update-state! owner (fn [state] (assoc state :welc-ol-vis false))))}
                  (dom/i #js {:className "fa fa-search"} "")
                  "search library")
                " collected by other readers."))))))))

;; <div class="book-preview-overlay">
;;     <div class="book-card-preview"></div>
;;   </div>

(defn landing-page [data owner]
  (reify
    om/IRender
    (render [this]
      (om/build books-grid (:items (:books data))))))
