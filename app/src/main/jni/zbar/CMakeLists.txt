
project(zbarjni)

include_directories(include zbar ${iconv_SOURCE_DIR}/include)

MESSAGE( "Build Lib ZBar for ${ANDROID_ABI} ${zbarjni_SOURCE_DIR} ${CMAKE_INCLUDE_PATH}" )
add_library(lib_gperf SHARED IMPORTED)
add_library(${PROJECT_NAME} SHARED
			zbarjni.c
			zbar/img_scanner.c
			zbar/decoder.c
			zbar/image.c
			zbar/symbol.c
			zbar/convert.c
			zbar/config.c
			zbar/scanner.c
			zbar/error.c
			zbar/refcnt.c
			zbar/video.c
			zbar/video/null.c
			zbar/decoder/code128.c
			zbar/decoder/code39.c
			zbar/decoder/code93.c
			zbar/decoder/codabar.c
			zbar/decoder/databar.c
			zbar/decoder/ean.c
			zbar/decoder/i25.c
			zbar/decoder/qr_finder.c
			zbar/qrcode/bch15_5.c
			zbar/qrcode/binarize.c
			zbar/qrcode/isaac.c
			zbar/qrcode/qrdec.c
			zbar/qrcode/qrdectxt.c
			zbar/qrcode/rs.c
			zbar/qrcode/util.c)

target_link_libraries(${PROJECT_NAME}
						iconv)


