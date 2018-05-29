//
// Created by yangd on 2018/5/17.
//

#ifndef SPEECHSPLIT_VAD_WRAPPER_H
#define SPEECHSPLIT_VAD_WRAPPER_H

#include <string>

bool Init();
bool RunVAD(const std::string& input_file, const std::string& output_dir,
           const std::string& output_filename_prefix);

#endif //SPEECHSPLIT_VAD_WRAPPER_H
