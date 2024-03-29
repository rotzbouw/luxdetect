# Luxembourgish Language Detection

Java service detecting language from text input. Idea stems from the often claimed idea that Luxembourgish sounds like or resembles either French, Dutch or German.

## Details
- Can be deployed as part of HTTP server taking POST requests (see below).
- Contains language models for Luxembourgish, German, French, Dutch and for completeness sake English
- Language models crated using https://github.com/rotzbouw/ngramlanguagemodel

### Web service
Once the application has started, you can request language detection like follows:

    $ curl localhost:8080 -X POST -H 'Content-Type: application/json' -d '{"text": "De Charly Gaul, gebuer den 8. Dezember 1932 am Pafendall a gestuerwen de 6. Dezember 2005 zu Lëtzebuerg, war e lëtzebuergesche Vëlossportler. Hien huet 1958 den Tour de France gewonnen."}'

and it will return something like this:

    {
      "scoredLanguages": {
        "LUXEMBOURGISH": -7684.8262319020905,
        "ENGLISH": -9661.92351756399,
        "GERMAN": -8846.610103666457,
        "FRENCH": -9576.47393955048,
        "DUTCH": -9015.457229347536
      }
    }

The scores are the sum of the logarithmic probabilities of the relative frequencies of each n-gram (n={1,5}).

## Evaluation
LanguageDetectionServiceTest loads five validation test sets extracted from Wortschatz corpora (see below), calls the language detection service for each sentence and counts positive as well as negative matches.

    ENGLISH: 200923 positive, 77 negative => 99.96169154228856
    GERMAN: 200877 positive, 123 negative => 99.93880597014926
    FRENCH: 200884 positive, 116 negative => 99.94228855721393
    DUTCH: 200856 positive, 144 negative => 99.92835820895522
    LUXEMBOURGISH: 200814 positive, 186 negative => 99.90746268656716

As we can see, it's not that hard to distinguish Luxembourgish from other, seemingly similar, languages. ;)

### Data
20% of shuffled -sentence files from the following archives:
1. ltz-lu_web_2013_1M.tar.gz
2. deu-com_web_2018_1M.tar.gz
3. nld-nl_web-public_2019_1M.tar.gz
4. fra-eu_web_2017_1M.tar.gz
5. eng-com_web-public_2018_1M.tar.gz
downloaded from https://wortschatz.uni-leipzig.de/en/download

## TODO
Nothing, really. Once the author of https://github.com/pemistahl/lingua documents the format of their language profiles, I will adapt it in https://github.com/rotzbouw/ngramlanguagemodel and create a model to be included in a pull request to the lingua project.