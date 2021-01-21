#!/bin/bash

# parameter: 1: root path to input dir (models) 2: root path to input dir (samples) 3: root path output dir (csv) 4: algorithm name 5: number of sampling run
#java -jar ./stabilityCalculator.jar 'C:\Users\t.pett\Documents\VaMoS_Data\test\models' 'C:\Users\t.pett\Documents\VaMoS_Data\test\samples' 'C:\Users\t.pett\Documents\VaMoS_Data\test\eval' 'incling' '0'

### Chvatal
java -jar ./stabilityCalculator.jar 'A:\220_Case_Studies\003_busybox-daily-case-study\010_models' 'A:\220_Case_Studies\003_busybox-daily-case-study\020_samples' 'A:\220_Case_Studies\003_busybox-daily-case-study\030_sampling_stability' 'chvatal' '0'

### ICPL
#java -jar ./stabilityCalculator.jar 'A:\220_Case_Studies\003_busybox-daily-case-study\010_models' 'A:\220_Case_Studies\003_busybox-daily-case-study\020_samples' 'A:\220_Case_Studies\003_busybox-daily-case-study\030_sampling_stability' 'icpl' '0'

### incling
#java -jar ./stabilityCalculator.jar 'A:\220_Case_Studies\003_busybox-daily-case-study\010_models' 'A:\220_Case_Studies\003_busybox-daily-case-study\020_samples' 'A:\220_Case_Studies\003_busybox-daily-case-study\030_sampling_stability' 'incling' '0'

### random
#java -jar ./stabilityCalculator.jar 'A:\220_Case_Studies\003_busybox-daily-case-study\010_models' 'A:\220_Case_Studies\003_busybox-daily-case-study\020_samples' 'A:\220_Case_Studies\003_busybox-daily-case-study\030_sampling_stability' 'random' '0'

### yasa
#java -jar ./stabilityCalculator.jar 'A:\220_Case_Studies\003_busybox-daily-case-study\010_models' 'A:\220_Case_Studies\003_busybox-daily-case-study\020_samples' 'A:\220_Case_Studies\003_busybox-daily-case-study\030_sampling_stability' 'yasa' '0'
