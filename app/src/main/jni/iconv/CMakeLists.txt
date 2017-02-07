project(iconv)

MESSAGE( "Build Lib Iconv fo ${ANDROID_ABI} ${CMAKE_CURRENT_SOURCE_DIR}" )

set(CMAKE_C_FLAGS "-Wno-multichar -DANDROID -DLIBDIR=\"c\" -DBUILDING_LIBICONV -DIN_LIBRARY")

include_directories(include libcharset lib libcharset/include srclib)

add_library(${PROJECT_NAME} SHARED
			libcharset/lib/localcharset.c
			lib/iconv.c
			lib/relocatable.c)


