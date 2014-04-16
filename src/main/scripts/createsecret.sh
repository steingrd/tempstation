#!/bin/bash
java -cp target/classes:target/dependency/* com.github.steingrd.fermonitor.commands.CreateSecret $@
