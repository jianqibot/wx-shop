package com.github.jianqibot.wxshop.controller;

import com.github.jianqibot.wxshop.entity.HttpException;
import com.github.jianqibot.wxshop.entity.PageResponse;
import com.github.jianqibot.wxshop.entity.Response;
import com.github.jianqibot.wxshop.generate.Shop;
import com.github.jianqibot.wxshop.service.ShopService;
import com.github.jianqibot.wxshop.service.UserContext;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1")
public class ShopController {
    private final ShopService shopService;
    @Inject
    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }
    /**
     * @api {get} /shop get all shops under current user
     * @apiName GetShop
     * @apiGroup shop
     * @apiHeader {String} Accept application/json
     * @apiParam {Number} pageNum pageNum page number, from 1
     * @apiParam {Number} pageSize pageSize page size, how many item in one page
     * @apiSuccess {Number} pageNum pageNum page number, from 1
     * @apiSuccess {Number} pageSize pageSize page size, how many item in one page
     * @apiSuccess {Number} totalPage page in total
     * @apiSuccess {Shop} data shop list
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 200 OK
     * {
     * "pageNum": 1,
     * "pageSize": 10,
     * "totalPage": 5,
     * "data": [
     * {
     * "id": 12345,
     * "name": "my shop",
     * "description": "my soap shop",
     * "imgUrl": "https://img.url",
     * "ownerUserId": 12345,
     * "createdAt": "2020-03-22T13:22:03Z",
     * "updatedAt": "2020-03-22T13:22:03Z"
     * },
     * {
     * ...
     * }
     * ]
     * }
     * @apiError 401 Unauthorized If user hasn't login yet
     * @apiErrorExample Error-Response:
     * HTTP/1.1 401 Unauthorized
     * {
     * "message": "Unauthorized"
     * }
     */

    /**
     * @param pageNum page number
     * @param pageSize page size
     * @return shop list
     */
    @GetMapping("/shop")
    @ResponseBody
    public PageResponse<Shop> getShops(@RequestParam("pageNum") Integer pageNum,
                                       @RequestParam("pageSize") Integer pageSize) {
        return shopService.getShop(pageNum, pageSize, UserContext.getCurrentUser().getId());
    }


    /**
     * @api {get} /shop/:id get shop with specified ID
     * @apiName GetShopById
     * @apiGroup shop
     * @apiHeader {String} Accept application/json
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 200 Created
     * {
     * "data": {
     * "id": 12345,
     * "name": "my shop",
     * "description": "my soap shop",
     * "imgUrl": "https://img.url",
     * "ownerUserId": 12345,
     * "createdAt": "2020-03-22T13:22:03Z",
     * "updatedAt": "2020-03-22T13:22:03Z"
     * }
     * }
     * @apiError 401 Unauthorized If user hasn't login yet
     * @apiError 404 Not found If shop not found
     * @apiErrorExample Error-Response:
     * HTTP/1.1 401 Unauthorized
     * {
     * "message": "Unauthorized"
     * }
     */
    /**
     * @param shopId shop ID
     * @param response http response
     * @return response of Shop type
     */
    @GetMapping("/shop/{id}")
    @ResponseBody
    public Response<Shop> getShop(@PathVariable("id") Long shopId,
                                  HttpServletResponse response) {
        try {
            return Response.of(shopService.getShopById(shopId));
        } catch (HttpException e) {
            response.setStatus(e.getStatusCode());
            return Response.error(e.getMessage());
        }
    }


    /**
     * @api {post} /shop create shop
     * @apiName CreateShop
     * @apiGroup shop
     * @apiHeader {String} Accept application/json
     * @apiHeader {String} Content-Type application/json
     * @apiParamExample {json} Request-Example:
     * {
     * "id": 12345,
     * "name": "my shop",
     * "description": "my soap shop",
     * "imgUrl": "https://img.url",
     * }
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 201 Created
     * {
     * "data": {
     * "id": 12345,
     * "name": "my shop",
     * "description": "my soap shop",
     * "imgUrl": "https://img.url",
     * "ownerUserId": 12345,
     * "createdAt": "2020-03-22T13:22:03Z",
     * "updatedAt": "2020-03-22T13:22:03Z"
     * }
     * }
     * @apiError 400 Bad Request If request contains error
     * @apiError 401 Unauthorized If user hasn't login yet
     * @apiErrorExample Error-Response:
     * HTTP/1.1 401 Unauthorized
     * {
     * "message": "Unauthorized"
     * }
     */
    /**
     * @param shop shop to be created
     * @param response http response
     * @return response of Shop type
     */
    @PostMapping("/shop")
    @ResponseBody
    public Response<Shop> createShop(@RequestBody Shop shop,
                                     HttpServletResponse response) {
        try {
            return Response.of(shopService.createShop(shop, UserContext.getCurrentUser().getId()));
        } catch (HttpException e) {
            response.setStatus(e.getStatusCode());
            return Response.error(e.getMessage());
        }
    }


    /**
     * @api {PATCH} /shop/:id modify shop
     * @apiName UpdateShop
     * @apiGroup shop
     * @apiParam {Number} id shop ID
     * @apiHeader {String} Accept application/json
     * @apiHeader {String} Content-Type application/json
     * @apiParamExample {json} Request-Example:
     * {
     * "id": 12345,
     * "name": "my shop",
     * "description": "my soap shop",
     * "imgUrl": "https://img.url",
     * }
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 200 OK
     * {
     * "data": {
     * "id": 12345,
     * "name": "my shop",
     * "description": "my soap shop",
     * "imgUrl": "https://img.url",
     * "ownerUserId": 12345,
     * "createdAt": "2020-03-22T13:22:03Z",
     * "updatedAt": "2020-03-22T13:22:03Z"
     * }
     * }
     * @apiError 400 Bad Request If request contains error
     * @apiError 401 Unauthorized If user hasn't login yet
     * @apiError 403 Forbidden If user tries to modify shop does not belong to him
     * @apiError 404 Not Found shop not found
     * @apiErrorExample Error-Response:
     * HTTP/1.1 401 Unauthorized
     * {
     * "message": "Unauthorized"
     * }
     */
    /**
     * @param shopId shop ID
     * @param shopForUpdate shop to be updated
     * @param response http reponse
     * @return response of Shop type
     */
    @PatchMapping("/shop/{id}")
    @ResponseBody
    public Response<Shop> modifyShop(@PathVariable("id") Long shopId,
                                     @RequestBody Shop shopForUpdate,
                                     HttpServletResponse response) {
        try {
            return Response.of(shopService.modifyShop(shopId, shopForUpdate, UserContext.getCurrentUser().getId()));
        } catch (HttpException e) {
            response.setStatus(e.getStatusCode());
            return Response.error(e.getMessage());
        }
    }


    /**
     * @api {DELETE} /shop/:id delete shop
     * @apiName DeleteShop
     * @apiGroup shop
     *
     * @apiHeader {String} Accept application/json
     *
     * @apiParam {Number} id shop ID
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 204 No Content
     *     {
     *       "data": {
     *              "id": 12345,
     *              "name": "my shop",
     *              "description": "my soap shop",
     *              "imgUrl": "https://img.url",
     *              "ownerUserId": 12345,
     *              "createdAt": "2020-03-22T13:22:03Z",
     *              "updatedAt": "2020-03-22T13:22:03Z"
     *       }
     *     }
     *
     * @apiError 400 Bad Request If request contains error
     * @apiError 401 Unauthorized If user hasn't login yet
     * @apiError 403 Forbidden If user tries to delete shop does not belong to him
     * @apiError 404 Not Found shop not found
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 401 Unauthorized
     *     {
     *       "message": "Unauthorized"
     *     }
     */
    /**
     * @param shopId shop ID
     * @param response http response
     * @return response of Shop type
     */
    @DeleteMapping("/shop/{id}")
    @ResponseBody
    public Response<Shop> deleteShop(@PathVariable("id") Long shopId,
                                     HttpServletResponse response) {
        try {
            return Response.of(shopService.deleteShop(shopId, UserContext.getCurrentUser().getId()));
        } catch (HttpException e) {
            response.setStatus(e.getStatusCode());
            return Response.error(e.getMessage());
        }
    }
}
