(ns callcongress.utils)

;;; Copied from mtu, really need to publish that..

(defn cl-find [val sequence & {xkey :key, xtest :test, :or {xkey identity, xtest =}}]
  (apply (some-fn #(and (xtest (xkey %) val) %)) sequence))

(defmacro ignore-errors "Execute `body`; if an exception occurs return `nil`. Note: strongly deprecated for production code."
  [& body]
  `(try (do ~@body)
        (catch Throwable e# nil)))

(defn safe-parse-integer [string]
  (ignore-errors
   (Integer. string)))

;;; XML utilities

;;; Recursive find (does not walk down matching elements)
(defn find-elts [pred elt]
  (if (pred elt)
    (list elt)
    (mapcat (partial find-elts pred) (:content elt))))

(defn elt-subs [tag elt]
  (filter #(= (:tag %) tag) (:content elt)))

(defn elt-sub [tag elt]
  (first (elt-subs tag elt)))

;;; Gets contents of a string-valued sub-element
(defn elt-sub-text [tag elt]
  (first (:content (elt-sub tag elt))))
