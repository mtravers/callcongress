(ns callcongress.utils)

;;; Copied from mtu, really need to publish that..

(defn cl-find [val sequence & {xkey :key, xtest :test, :or {xkey identity, xtest =}}]
  (apply (some-fn #(and (xtest (xkey %) val) %)) sequence))
