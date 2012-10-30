(ns ziputil.core
  (:require [clojure.java.io :as io])
  (:import [java.util.zip ZipEntry ZipOutputStream]))

(def ^:dynamic *buffer-size* 10240)

(defn create-zip-file
  "Write a compressed zip archive of `entries` to `output-file`. `entries` should
   be a list of name/data pairs, where name is the name of the zipfile entry and
   value anything that can be opened with clojure.java.io/input-stream. For
   example:

  (create-zip-file \"foo.zip\" \"file1.txt\" file1 \"file2.txt\" file2"
  [output-file & entries]
  (when (odd? (count entries))
    (throw (IllegalArgumentException. "create-zip-file requires an even-numbered list of name/data pairs")))
  (let [buffer (byte-array *buffer-size*)]
    (with-open [zip-output-stream (ZipOutputStream. (io/output-stream output-file))]
      (.setLevel zip-output-stream 9)
      (doseq [[entry-name entry-data] (partition 2 entries)]
        (.putNextEntry zip-output-stream (ZipEntry. (name entry-name)))
        (with-open [input (io/input-stream entry-data)]
          (loop []
            (let [c (.read input buffer 0 *buffer-size*)]
              (when (not= -1 c)
                (.write zip-output-stream buffer 0 c)
                (recur)))))
        (.closeEntry zip-output-stream))))
  true)
