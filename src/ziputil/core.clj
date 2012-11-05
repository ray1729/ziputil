(ns ziputil.core
  (:require [clojure.java.io :as io])
  (:import [java.util.zip ZipEntry ZipOutputStream Deflater]))

(def ^:dynamic *buffer-size* 10240)

(def best-speed          Deflater/BEST_SPEED)
(def best-compression    Deflater/BEST_COMPRESSION)
(def default-compression Deflater/DEFAULT_COMPRESSION)

(defn create-archive*
  [output-file {:keys [compression-level prefix] :or {compression-level default-compression prefix ""}} entries]
  (let [buffer (byte-array *buffer-size*)]
    (with-open [zip-output-stream (ZipOutputStream. (io/output-stream output-file))]
      (.setLevel zip-output-stream compression-level)
      (doseq [[entry-name entry-data] (partition 2 entries)]
        (.putNextEntry zip-output-stream (ZipEntry. (str prefix (name entry-name))))
        (with-open [input (io/input-stream entry-data)]
          (loop []
            (let [bytes-read (.read input buffer 0 *buffer-size*)]
              (when (not= -1 bytes-read)
                (.write zip-output-stream buffer 0 bytes-read)
                (recur)))))
        (.closeEntry zip-output-stream))))
  output-file)

(defn create-archive
  "Write a compressed zip archive of `entries` to `output-file`. `entries` should
   be a list of `name`/`data` pairs, where `name` is the name of the zipfile entry and
   `data` anything that can be opened with clojure.java.io/input-stream. For
   example:
  (create-arhcive \"foo.zip\" \"file1.txt\" file1 \"file2.txt\" file2)"
  [output-file & args]
  (let [[opts entries] (if (map? (first args))
                         [(first args) (rest args)]
                         [{} args])]
    (when (odd? (count entries))
      (throw (IllegalArgumentException. "create-archive requires an even-numbered list of name/data pairs")))
    (create-archive* output-file opts entries)))
