import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IComment, NewComment } from '../comment.model';

export type PartialUpdateComment = Partial<IComment> & Pick<IComment, 'id'>;

export type EntityResponseType = HttpResponse<IComment>;
export type EntityArrayResponseType = HttpResponse<IComment[]>;

@Injectable({ providedIn: 'root' })
export class CommentService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/comments');

  create(comment: NewComment): Observable<EntityResponseType> {
    return this.http.post<IComment>(this.resourceUrl, comment, { observe: 'response' });
  }

  update(comment: IComment): Observable<EntityResponseType> {
    return this.http.put<IComment>(`${this.resourceUrl}/${this.getCommentIdentifier(comment)}`, comment, { observe: 'response' });
  }

  partialUpdate(comment: PartialUpdateComment): Observable<EntityResponseType> {
    return this.http.patch<IComment>(`${this.resourceUrl}/${this.getCommentIdentifier(comment)}`, comment, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IComment>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IComment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCommentIdentifier(comment: Pick<IComment, 'id'>): string {
    return comment.id;
  }

  compareComment(o1: Pick<IComment, 'id'> | null, o2: Pick<IComment, 'id'> | null): boolean {
    return o1 && o2 ? this.getCommentIdentifier(o1) === this.getCommentIdentifier(o2) : o1 === o2;
  }

  addCommentToCollectionIfMissing<Type extends Pick<IComment, 'id'>>(
    commentCollection: Type[],
    ...commentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const comments: Type[] = commentsToCheck.filter(isPresent);
    if (comments.length > 0) {
      const commentCollectionIdentifiers = commentCollection.map(commentItem => this.getCommentIdentifier(commentItem));
      const commentsToAdd = comments.filter(commentItem => {
        const commentIdentifier = this.getCommentIdentifier(commentItem);
        if (commentCollectionIdentifiers.includes(commentIdentifier)) {
          return false;
        }
        commentCollectionIdentifiers.push(commentIdentifier);
        return true;
      });
      return [...commentsToAdd, ...commentCollection];
    }
    return commentCollection;
  }
}
